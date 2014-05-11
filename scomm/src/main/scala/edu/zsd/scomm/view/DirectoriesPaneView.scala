package edu.zsd.scomm.view

import scala.swing.{Orientation, SplitPane}
import java.nio.file.Path
import edu.zsd.scomm.controller.DirectoryList

class DirectoriesPaneView(initLeftDir : Path, initRightDir : Path) extends SplitPane {

  name = "directoriesPane"
  val leftList = new DirectoryList("directoriesPane.left", initLeftDir)
  val rightList = new DirectoryList("directoriesPane.right", initRightDir)

  val left = leftList.view
  val right = rightList.view
  
  leftComponent = left
  rightComponent = right
  orientation = Orientation.Vertical
  dividerLocation = 0.5
  resizeWeight = 0.5

}
