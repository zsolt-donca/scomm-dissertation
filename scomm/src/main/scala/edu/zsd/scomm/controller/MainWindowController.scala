package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{SelectionInfo, DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.{Paths, FileAlreadyExistsException, Path, Files}
import javax.swing.JOptionPane
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.CPSUtils._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import edu.zsd.scomm.operations.{CopyPanel, DeletePanel, NewFolderPanel}


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val newFolderPanel: NewFolderPanel,
                                        val deletePanel: DeletePanel,
                                        val copyPanel: CopyPanel) extends Observing {

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

  val newFolderLoop = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.newFolderButton()

      newFolderPanel.reset()
      view.argumentsPanel() = Some(newFolderPanel)

      self.abortOn(newFolderPanel.cancelButton()) {
        val activeList: DirectoryListModel = model.directoriesPaneModel.activeList.now
        val currentDir: Path = activeList.currentDir.now

        var repeat = true
        while (repeat) {
          self awaitNext newFolderPanel.okButton()
          val folderName: String = newFolderPanel.folderName.text
          val newFolderPath: Path = currentDir.resolve(folderName)
          suspendable_block {
            try {
              Files.createDirectory(newFolderPath)
              diskState.refresh()
              model.status() = s"Successfully created folder '$folderName'!"
              activeList.selectedPaths() = Set(newFolderPath)
              repeat = false
            } catch {
              case e: FileAlreadyExistsException =>
                model.status() = s"Folder '$folderName' already exists!"
              case e: IOException =>
                model.status() = "Error: " + e.getMessage
            }
            unit()
          }
        }
        unit()
      }

      view.argumentsPanel() = None
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
        val sourceDir = directoryListModel.currentDir.now
        val selectionInfo: SelectionInfo = directoryListModel.selectionInfo.now
        val destinationDir: Path = Paths.get(copyPanel.destination.text)

        self awaitNext copyPanel.okButton()
        walkPathsPreOrder(selectionInfo.paths) {
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

      view.argumentsPanel() = None
  }
}
