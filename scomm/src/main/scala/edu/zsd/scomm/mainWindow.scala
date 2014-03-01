
package edu.zsd.scomm

import java.io.File

import scala.swing._

object mainWindow extends SimpleSwingApplication {

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

    contents = new SplitPane() {
      val left = new DirectoryListController(new File("C:\\"))
      leftComponent = left.directoryListView

      val right = new DirectoryListController(new File("D:\\"))
      rightComponent = right.directoryListView

      orientation = Orientation.Vertical
      dividerLocation = 0.5
      resizeWeight = 0.5
    }

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}
