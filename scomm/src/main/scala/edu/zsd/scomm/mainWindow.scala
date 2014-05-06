
package edu.zsd.scomm


import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import javax.swing.JOptionPane
import java.io.File

object mainWindow extends ReactiveSimpleSwingApplication with Observing {

  var initLeftDir : File = new File("C:\\")
  var initRightDir : File = new File("D:\\")
  override def startup(args: Array[String]): Unit = {
    if (args.length >= 1) {
      val initDir = new File(args(0))
      initLeftDir = initDir
      initRightDir = initDir
    }
    super.startup(args)
  }

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

    val directoriesPane = new DirectoriesPane("directoriesPane", initLeftDir, initRightDir)

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

      observe(directoriesPane.left.selectedFiles) {
        selectedFiles => commandButtons.infoButton.enabled = selectedFiles.nonEmpty
      }

      val infoActionReactor = Reactor.loop {
        self =>
          self awaitNext commandButtons.infoButton.actionEvents
          val selectedFiles: Seq[FileEntry] = directoriesPane.left.selectedFiles.now
          val directories = selectedFiles.count(fileEntry => fileEntry.file.isDirectory)
          val files = selectedFiles.count(fileEntry => fileEntry.file.isFile)
          val message = s"There are $directories directories and $files files selected in ${directoriesPane.left.currentDir.now}"
          JOptionPane.showMessageDialog(null, message, "View", JOptionPane.INFORMATION_MESSAGE)
      }
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}



