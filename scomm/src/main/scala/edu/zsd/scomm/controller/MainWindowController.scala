package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{SelectionInfo, DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.{Paths, Path, Files}
import javax.swing.JOptionPane
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.Utils._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import edu.zsd.scomm.operations.{NewFolderOperation, CopyPanel, DeletePanel, NewFolderPanel}
import com.typesafe.scalalogging.slf4j.StrictLogging


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val newFolderPanel: NewFolderPanel,
                                        val deletePanel: DeletePanel,
                                        val copyPanel: CopyPanel,
                                        val directoriesPaneController: DirectoriesPaneController)
  extends Observing with StrictLogging {

  val infoActionReactor = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.infoButton()
      val activeList: DirectoryListModel = view.directoriesPane.model.activeList.now
      val selectionInfo = activeList.selectionInfo.now
      val directories = selectionInfo.folders
      val files = selectionInfo.files
      val message = s"There are $directories directories and $files files selected in ${activeList.currentDir.now}"
      JOptionPane.showMessageDialog(null, message, "View", JOptionPane.INFORMATION_MESSAGE)
  }

  observe(view.commandButtons.refreshButton()) {
    _ =>
      diskState.refresh()
  }

  val newFolderLoop = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.newFolderButton()

      newFolderPanel.reset()
      view.argumentsPanel() = Some(newFolderPanel)
      val activeListModel: DirectoryListModel = model.directoriesPaneModel.activeList.now
      val currentDir: Path = activeListModel.currentDir.now

      self.abortOn(newFolderPanel.cancelButton()) {

        var repeat = true
        while (repeat) {
          self awaitNext newFolderPanel.okButton()
          val folderName: String = newFolderPanel.folderName.text
          val newFolderPath: Path = currentDir.resolve(folderName)

          val op = new NewFolderOperation(newFolderPath)
          val result = op.execute()
          result match {
            case NewFolderOperation.Success() =>
              diskState.refresh()
              model.status() = s"Successfully created folder '$folderName'!"
              activeListModel.selectedPaths() = Set(newFolderPath)
              repeat = false
            case NewFolderOperation.FileAlreadyExists() =>
              model.status() = s"Folder '$folderName' already exists!"
            case NewFolderOperation.GenericError(e) =>
              model.status() = "Error: " + e.getMessage
          }
        }
      }

      view.argumentsPanel() = None

      //      val activeListView = view.directoriesPane.activeList.now
      //      logger.info(s"Requesting focus to active list: $activeListView")
      //      directoriesPaneController.setFocusTo(left = false)
      unit()
  }

  val deleteLoop = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.deleteButton()

      deletePanel.reset()
      view.argumentsPanel() = Some(deletePanel)

      self.abortOn(deletePanel.cancelButton()) {
        val selectionInfo: SelectionInfo = model.directoriesPaneModel.activeList.now.selectionInfo.now

        self awaitNext deletePanel.okButton()
        walkPathsPostOrder(selectionInfo.paths) {
          path =>
            try {
              model.status() = s"Deleting '$path'..."
              Files.setAttribute(path, "dos:readonly", false)
              Files.delete(path)
              self.pause
            } catch {
              case e: IOException => e.printStackTrace()
            }
        }
        diskState.refresh()

        model.status() = s"Successfully deleted!"
      }

      view.argumentsPanel() = None
  }

  val copyLoop = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.copyButton()

      copyPanel.reset()
      view.argumentsPanel() = Some(copyPanel)

      self.abortOn(copyPanel.cancelButton()) {
        val directoryListModel = model.directoriesPaneModel.activeList.now
        val selectionInfo: SelectionInfo = directoryListModel.selectionInfo.now

        val sourceDir = directoryListModel.currentDir.now
        val sourceDirs: Set[Path] = selectionInfo.paths

        self awaitNext copyPanel.okButton()

        val destinationDir: Path = Paths.get(copyPanel.destination.text)

        val wrong: Set[Path] = sourceDirs.filter(dir => destinationDir.startsWith(dir))
        if (wrong.nonEmpty) {
          model.status() = "You cannot copy a directory to its own subdirectory!"
        } else {

          walkPathsPreOrder(sourceDirs) {
            sourcePath =>
              try {
                val destinationPath = destinationDir.resolve(sourceDir.relativize(sourcePath))

                if (Files.isRegularFile(sourcePath)) {
                  model.status() = s"Copying '$sourcePath' to '${destinationPath.getParent}'..."
                  Files.copy(sourcePath, destinationPath)
                } else if (Files.isDirectory(sourcePath)) {
                  model.status() = s"Creating directory '$destinationPath'..."
                  Files.createDirectory(destinationPath)
                }
                self.pause
              } catch {
                case e: IOException => e.printStackTrace()
              }
          }
          diskState.refresh()

          model.status() = s"Successfully copied!"
        }
      }

      view.argumentsPanel() = None
  }
}
