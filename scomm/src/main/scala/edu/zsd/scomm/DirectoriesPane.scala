package edu.zsd.scomm

import scala.swing.{Publisher, Orientation, SplitPane}
import java.io.File
import scala.swing.event.{MouseClicked, FocusLost, FocusGained}

class DirectoriesPane(componentName : String, initLeftDir : File, initRightDir : File) extends SplitPane {

  name = componentName
  val leftList = new DirectoryList(componentName + ".left", initLeftDir)
  val rightList = new DirectoryList(componentName + ".right", initRightDir)

  val left = leftList.view
  val right = rightList.view
  
  leftComponent = left
  rightComponent = right
  orientation = Orientation.Vertical
  dividerLocation = 0.5
  resizeWeight = 0.5

}
