package edu.zsd.scomm.controller

import edu.zsd.scomm.view.{NewFolderPanel, MainWindowView}
import edu.zsd.scomm.model.{DiskState, MainWindowModel, DirectoryListModel, FileEntry}
import java.nio.file.{FileAlreadyExistsException, Path, Files}
import javax.swing.JOptionPane
import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val newFolderPanel: NewFolderPanel) extends Observing {

  val infoActionReactor = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.infoButton()
      val activeList: DirectoryListModel = view.directoriesPane.model.activeList.now
      val selectedFiles: Seq[FileEntry] = activeList.selectedFiles.now
      val directories = selectedFiles.count(fileEntry => Files.isDirectory(fileEntry.path))
      val files = selectedFiles.count(fileEntry => Files.isRegularFile(fileEntry.path))
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

        var close = false
        while (!close) {
          self awaitNext newFolderPanel.okButton()
          val folderName: String = newFolderPanel.folderName.text

          val newFolderPath: Path = currentDir.resolve(folderName)
          cps_try {
            try {
              Files.createDirectory(newFolderPath)
              diskState.refresh()
              model.status() = s"Successfully created folder '$folderName'!"
              close = true
            } catch {
              case e: FileAlreadyExistsException => model.status() = s"Folder '$folderName' already exists!";
              case e: IOException => model.status() = "Error: " + e.getMessage;
            }
            unit()
          }
          unit()
        }
      }

      view.argumentsPanel() = None
  }
}
