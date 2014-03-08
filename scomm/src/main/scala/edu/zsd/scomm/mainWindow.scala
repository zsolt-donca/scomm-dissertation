
package edu.zsd.scomm


import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import javax.swing.JOptionPane

object mainWindow extends ReactiveSimpleSwingApplication with Observing {

  override def top: Frame = new MainFrame {

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

    val directoriesPane = new DirectoriesPane()

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
        contents += infoButton
      }
      add(commandButtons, Position.South)

      val infoActionReactor = Reactor.loop {
        self =>
          self awaitNext commandButtons.infoButton.actionEvents
          val selectedFiles: Seq[FileEntry] = directoriesPane.left.selectedFiles.now
          val directories = selectedFiles.count(fileEntry => fileEntry.file.isDirectory)
          val files = selectedFiles.count(fileEntry => fileEntry.file.isFile)
          val message = s"There are $directories directories and $files files selected in ${directoriesPane.left.currentDir.now}"
          JOptionPane.showMessageDialog(null, message)
      }
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}



