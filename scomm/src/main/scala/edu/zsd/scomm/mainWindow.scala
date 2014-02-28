
package edu.zsd.scomm

import java.io.File

import scala.swing._

object mainWindow extends SimpleSwingApplication {

  override def top: Frame = new MainFrame {

    val directoryListViewController = new DirectoryListController(new File("D:\\"))

    menuBar = new MenuBar() {
      contents += new Menu("Files") {
        contents += new MenuItem("Quit")
      }
      contents += new Menu("Drives") {
        contents += new MenuItem(createMoveAction("C:\\"))
        contents += new MenuItem(createMoveAction("D:\\"))
      }
      contents += new Menu("Help") {
        contents += new MenuItem("About")
      }
    }

    def createMoveAction(path: String) = new Action("Move to " + path) {
      override def apply(): Unit = directoryListViewController.directoryListModel.currentDir() = new File(path)
    }

    //    contents = new SplitPane() {
    //      val left = new DirectoryListController(new File("C:\\")) {
    //        override def toString: String = "left DirectoryListController"
    //      }
    //      leftComponent = left.directoryListView
    //
    //      val right = new DirectoryListController(new File("D:\\")) {
    //        override def toString: String = "right DirectoryListController"
    //      }
    //      rightComponent = right.directoryListView
    //
    //      orientation = Orientation.Vertical
    //      dividerLocation = 0.5
    //      resizeWeight = 0.5
    //    }

    contents = directoryListViewController.directoryListView

    title = "Scala Commander"
    preferredSize = new Dimension(800, 500)
    pack()
    peer.setLocationRelativeTo(null)
  }
}
