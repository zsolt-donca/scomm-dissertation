package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{MainWindowModel, DirectoryListModel, FileEntry}
import java.nio.file.{Path, Files}
import javax.swing.JOptionPane
import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.actions.NewFolder


@Component
class MainWindowController @Autowired() (val model: MainWindowModel,
                                         val view: MainWindowView) extends Observing {

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
      val activeList : DirectoryListModel = model.directoriesPaneModel.activeList.now
      val currentDir : Path = activeList.currentDir.now

      val newFolder = new NewFolder(view)

      newFolder.dialog.visible = true
      self awaitNext newFolder.dialog.okButton()
      val folderName : String = newFolder.dialog.folderName.text
      newFolder.dialog.dispose()

      val newFolderPath: Path = currentDir.resolve(folderName)
      createDirectory(newFolderPath)
  }

  def createDirectory(newFolderPath: Path) {
    Files.createDirectory(newFolderPath)
  }
}
