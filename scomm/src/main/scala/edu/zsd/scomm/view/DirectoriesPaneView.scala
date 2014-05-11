package edu.zsd.scomm.view

import scala.swing.{Orientation, SplitPane}
import edu.zsd.scomm.model.DirectoriesPaneModel

class DirectoriesPaneView(val model : DirectoriesPaneModel,
                          val leftDirectoryListView : DirectoryListView,
                          val rightDirectoryListView : DirectoryListView) extends SplitPane {

  name = "directoriesPane"
  leftComponent = leftDirectoryListView
  rightComponent = rightDirectoryListView
  orientation = Orientation.Vertical
  dividerLocation = 0.5
  resizeWeight = 0.5

}
