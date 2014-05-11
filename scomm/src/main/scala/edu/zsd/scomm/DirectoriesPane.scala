package edu.zsd.scomm

import scala.swing.{Orientation, SplitPane}
import java.nio.file.Path

class DirectoriesPane(componentName : String, initLeftDir : Path, initRightDir : Path) extends SplitPane {

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
