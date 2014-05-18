package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{SelectionInfo, DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.{DirectoryStream, FileAlreadyExistsException, Path, Files}
import javax.swing.JOptionPane
import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import edu.zsd.scomm.operations.{DeletePanel, NewFolderPanel}
import scala.collection.JavaConverters._
import scala.util.continuations.suspendable


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val newFolderPanel: NewFolderPanel,
                                        val deletePanel: DeletePanel) extends Observing {

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
        deleteRecursively(selectionInfo.paths, self)
        diskState.refresh()

        model.status() = s"Successfully deleted!"
      }

      view.argumentsPanel() = None
  }

  def deleteRecursively(paths: Iterable[Path], flowOps: FlowOps): Unit@suspendable = {
    // instead of 'for (path: Path <- paths)'
    paths.cps foreach {
      path =>
        suspendable_block {
          if (Files.isDirectory(path)) {
            val directoryStream: DirectoryStream[Path] = Files.newDirectoryStream(path)
            try {
              deleteRecursively(directoryStream.asScala, flowOps)
              // instead of finally
              directoryStream.close()
            } catch {
              // instead of finally
              case e: Throwable => directoryStream.close(); throw e
            }
          }
        }
        suspendable_block {
          try {
            model.status() = s"Deleting '$path'..."
            Files.delete(path)
            flowOps.pause
          } catch {
            case e: IOException => e.printStackTrace()
          }
        }
    }
  }
}
