package edu.zsd.scomm.controller

import edu.zsd.scomm.view.{NewFolderPane, MainWindowView}
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
                                        val diskState: DiskState) extends Observing {

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

  val newFolder = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.newFolderButton()
      val activeList: DirectoryListModel = model.directoriesPaneModel.activeList.now
      val currentDir: Path = activeList.currentDir.now

      val newFolder = new NewFolderPane

      view.argumentsPane.panel = newFolder
      view.pack()
      val close = EventSource[Unit]

      self.loopUntil(close) {
        self awaitNext newFolder.okButton()
        val folderName: String = newFolder.folderName.text

        val newFolderPath: Path = currentDir.resolve(folderName)
        cps_closure {
          try {
            Files.createDirectory(newFolderPath)
            newFolder.message.text = "Success!"
            diskState.refresh()
            view.argumentsPane.clearPanel()
            view.pack()
            close << Unit
          } catch {
            case e: FileAlreadyExistsException => newFolder.message.text = "File already exists!"; view.pack();
            case e: IOException => newFolder.message.text = "Error: " + e.getMessage; view.pack();
          }
          println()
        }

        println()
      }
      println()
  }
}
