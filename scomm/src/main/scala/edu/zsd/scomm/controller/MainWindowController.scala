package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.{FileAlreadyExistsException, Path, Files}
import javax.swing.JOptionPane
import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import edu.zsd.scomm.operations.NewFolderPanel


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val newFolderPanel: NewFolderPanel) extends Observing {

  val infoActionReactor = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.infoButton()
      val activeList: DirectoryListModel = view.directoriesPane.model.activeList.now
      val selectedPaths: Set[Path] = activeList.selectedPaths.now
      val directories = selectedPaths.count(path => Files.isDirectory(path))
      val files = selectedPaths.count(path => Files.isRegularFile(path))
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
          suspendable_try {
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
}
