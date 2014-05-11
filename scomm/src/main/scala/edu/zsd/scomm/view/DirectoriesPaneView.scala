package edu.zsd.scomm.view

import scala.swing.{Orientation, SplitPane}
import java.nio.file.Path
import edu.zsd.scomm.controller.DirectoryList

class DirectoriesPaneView(val leftDirectoryList : DirectoryList, val rightDirectoryList : DirectoryList) extends SplitPane {

  val left = leftDirectoryList.view
  val right = rightDirectoryList.view

  name = "directoriesPane"
  leftComponent = left
  rightComponent = right
  orientation = Orientation.Vertical
  dividerLocation = 0.5
  resizeWeight = 0.5

}
