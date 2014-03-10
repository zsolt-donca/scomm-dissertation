package edu.zsd.scomm

import scala.swing.{Publisher, Orientation, SplitPane}
import java.io.File
import scala.swing.event.{MouseClicked, FocusLost, FocusGained}

class DirectoriesPane(componentName : String, initLeftDir : File, initRightDir : File) extends SplitPane {

  name = componentName
  val left  = new DirectoryList(componentName + ".left", initLeftDir)
  val right = new DirectoryList(componentName + ".right", initRightDir)
  
  leftComponent = left
  rightComponent = right
  orientation = Orientation.Vertical
  dividerLocation = 0.5
  resizeWeight = 0.5

  // focus
  val directoryLists = Seq(left, right)

  for(directoryList <- directoryLists) {
    listenTo(directoryList.mouse.clicks, directoryList.keys)
    reactions += {
      case MouseClicked(`directoryList`, _, _, _,  _) => println("mouse clicked"); setFocusTo(directoryList)
      case FocusGained(`directoryList`, _, _) => println("focus gained")

      case FocusLost(`directoryList`, _, _) => println("focus lost")
    }
  }

  def setFocusTo(directoryList: DirectoryList): Unit = {
    // directoryList already has focus
    // clear selection in others
    for (list <- directoryLists if list != directoryList) {
      list.listView.selectIndices()
    }
  }
}
