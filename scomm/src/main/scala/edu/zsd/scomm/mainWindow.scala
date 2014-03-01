
package edu.zsd.scomm

import java.io.File

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position

object mainWindow extends ReactiveSimpleSwingApplication {

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

    val left = new DirectoryListController(new File("C:\\"))
    val right = new DirectoryListController(new File("D:\\"))

    contents = new BorderPanel() {
      val centerSplitPane = new SplitPane() {
        leftComponent = left.directoryListView
        rightComponent = right.directoryListView
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

        val infoButton = new Button("Info")
        contents += infoButton
      }
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}
