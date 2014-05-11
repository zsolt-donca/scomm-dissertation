package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.FileEntry
import java.nio.file.Files
import javax.swing.JOptionPane
import edu.zsd.scomm.domain._


class MainWindow(val mainWindowView : MainWindowView) extends Observing {

  observe(mainWindowView.directoriesPane.model.left.selectedFiles) {
    selectedFiles => mainWindowView.commandButtons.infoButton.enabled = selectedFiles.nonEmpty
  }

  val infoActionReactor = Reactor.loop {
    self =>
      self awaitNext mainWindowView.commandButtons.infoButton.actionEvents
      val selectedFiles: Seq[FileEntry] = mainWindowView.directoriesPane.model.left.selectedFiles.now
      val directories = selectedFiles.count(fileEntry => Files.isDirectory(fileEntry.path))
      val files = selectedFiles.count(fileEntry => Files.isRegularFile(fileEntry.path))
      val message = s"There are $directories directories and $files files selected in ${mainWindowView.directoriesPane.model.left.currentDir.now}"
      JOptionPane.showMessageDialog(null, message, "View", JOptionPane.INFORMATION_MESSAGE)
  }

}
