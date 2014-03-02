
package edu.zsd.scomm

import java.io.File

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

    private val leftDirectoryList: DirectoryList = new DirectoryList(new File("C:\\"))
    private val rightDirectoryList: DirectoryList = new DirectoryList(new File("D:\\"))
    private val infoAction = EventSource[Unit]
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
        val viewButton = new Button("View")
        contents += viewButton

        val editButton = new Button("Edit")
        contents += editButton

        val copyButton = new Button("Copy")
        contents += copyButton

        val moveButton = new Button("Move")
        contents += moveButton

        val newFolderButton = new Button("New folder")
        contents += newFolderButton

        val deleteButton = new Button("Delete")
        contents += deleteButton

        val infoButton = new Button(Action("Info") {
          infoAction << Unit
        })
        contents += infoButton
      }
      add(commandButtons, Position.South)
    }

    val calcInfo = Strict {
      val selectedFiles: Seq[FileEntry] = leftDirectoryList.selectedFiles()

      val directories = selectedFiles.count(fileEntry => fileEntry.file.isDirectory)
      val files = selectedFiles.count(fileEntry => fileEntry.file.isFile)
      s"There are $directories directories and $files files selected in ${leftDirectoryList.currentDir()}"
    }

    val infoActionReactor = Reactor.loop {
      self =>
        self awaitNext infoAction
        val message = calcInfo()
        JOptionPane.showMessageDialog(null, message)
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}
