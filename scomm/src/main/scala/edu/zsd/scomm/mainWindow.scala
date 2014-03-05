
package edu.zsd.scomm

import java.io.File

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import javax.swing.JOptionPane
import scala.swing.event.{FocusLost, FocusGained}
import java.awt.event.FocusEvent

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

    private val leftDirectoryList  = new DirectoryList(new File("C:\\"))
    private val rightDirectoryList = new DirectoryList(new File("D:\\"))

    leftDirectoryList.focusable = true
    rightDirectoryList.focusable = true
    listenTo(leftDirectoryList, rightDirectoryList)
    reactions += {
      case FocusGained(_, _, _) => println("focus gained")
      case FocusLost(_, _, _) => println("focus lost")
    }

    contents = new BorderPanel() {
      val centerSplitPane = new SplitPane() {
        leftComponent = leftDirectoryList
        rightComponent = rightDirectoryList
        orientation = Orientation.Vertical
        dividerLocation = 0.5
        resizeWeight = 0.5
      }
      add(centerSplitPane, Position.Center)

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
          val selectedFiles: Seq[FileEntry] = leftDirectoryList.selectedFiles.now
          val directories = selectedFiles.count(fileEntry => fileEntry.file.isDirectory)
          val files = selectedFiles.count(fileEntry => fileEntry.file.isFile)
          val message = s"There are $directories directories and $files files selected in ${leftDirectoryList.currentDir.now}"
          JOptionPane.showMessageDialog(null, message)
      }
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}
