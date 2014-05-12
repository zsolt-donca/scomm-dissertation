package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{MainWindowModel, DirectoryListModel, FileEntry}
import java.nio.file.Files
import javax.swing.JOptionPane
import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired


@Component
class MainWindowController @Autowired() (val model: MainWindowModel,
                                         val view: MainWindowView) extends Observing {

  val infoActionReactor = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.infoButton.actionEvents
      val activeListOption: Option[DirectoryListModel] = view.directoriesPane.model.activeList.now
      val message = activeListOption match {
        case Some(activeList) =>
          val selectedFiles: Seq[FileEntry] = activeList.selectedFiles.now
          val directories = selectedFiles.count(fileEntry => Files.isDirectory(fileEntry.path))
          val files = selectedFiles.count(fileEntry => Files.isRegularFile(fileEntry.path))
          s"There are $directories directories and $files files selected in ${activeList.currentDir.now}"
        case None =>
          "No active directory list."
      }
      JOptionPane.showMessageDialog(null, message, "View", JOptionPane.INFORMATION_MESSAGE)
  }

}
