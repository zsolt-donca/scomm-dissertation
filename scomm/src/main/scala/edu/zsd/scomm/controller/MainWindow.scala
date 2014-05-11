package edu.zsd.scomm.controller

import scala.swing._
import scala.swing.BorderPanel.Position
import java.nio.file.Files
import javax.swing.JOptionPane
import java.awt.Dimension
import edu.zsd.scomm.domain._
import edu.zsd.scomm.model.FileEntry
import edu.zsd.scomm.view.{DirectoriesPaneView, EventButton}
import edu.zsd.scomm.AppParams

/**
 * Controller.
 */
class MainWindow(directoriesPane : DirectoriesPaneView) extends Observing {

  val frame = new MainFrame {

    menuBar = new MenuBar() {
      contents += new Menu("Files") {
        contents += new MenuItem("Quit")
      }
      contents += new Menu("Selection") {
        contents += new MenuItem("Select All")
        contents += new MenuItem("Unselect All")
        contents += new MenuItem("Invert Selection")
      }
      contents += new Menu("Help") {
        contents += new MenuItem("About")
      }
      contents += new Menu("System") {
        contents += new MenuItem(new Action("gc()") {
          override def apply(): Unit = System.gc()
        })
      }
    }

    contents = new BorderPanel() {
      add(directoriesPane, Position.Center)

      val commandButtons = new FlowPanel(FlowPanel.Alignment.Left)() {
        val viewButton = new EventButton("View")
        contents += viewButton

        val editButton = new EventButton("Edit")
        contents += editButton

        val copyButton = new EventButton("Copy")
        contents += copyButton

        val moveButton = new EventButton("Move")
        contents += moveButton

        val newFolderButton = new EventButton("New folder")
        contents += newFolderButton

        val deleteButton = new EventButton("Delete")
        contents += deleteButton

        val infoButton = new EventButton("Info")
        infoButton.name = "infoButton"
        contents += infoButton
      }
      add(commandButtons, Position.South)

      observe(directoriesPane.leftDirectoryList.model.selectedFiles) {
        selectedFiles => commandButtons.infoButton.enabled = selectedFiles.nonEmpty
      }

      val infoActionReactor = Reactor.loop {
        self =>
          self awaitNext commandButtons.infoButton.actionEvents
          val selectedFiles: Seq[FileEntry] = directoriesPane.leftDirectoryList.model.selectedFiles.now
          val directories = selectedFiles.count(fileEntry => Files.isDirectory(fileEntry.path))
          val files = selectedFiles.count(fileEntry => Files.isRegularFile(fileEntry.path))
          val message = s"There are $directories directories and $files files selected in ${directoriesPane.leftDirectoryList.model.currentDir.now}"
          JOptionPane.showMessageDialog(null, message, "View", JOptionPane.INFORMATION_MESSAGE)
      }
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }


}
