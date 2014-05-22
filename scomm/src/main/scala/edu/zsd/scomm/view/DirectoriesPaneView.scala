package edu.zsd.scomm.view

import scala.swing.{Orientation, SplitPane}
import edu.zsd.scomm.model.DirectoriesPaneModel
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.Domain._

@Component
class DirectoriesPaneView @Autowired() (val model : DirectoriesPaneModel,
                                        val leftDirectoryListView : LeftDirectoryListView,
                                        val rightDirectoryListView: RightDirectoryListView) extends SplitPane with Observing {

  name = "directoriesPane"
  leftComponent = leftDirectoryListView
  rightComponent = rightDirectoryListView
  orientation = Orientation.Vertical
  dividerLocation = 0.5
  resizeWeight = 0.5

  val activeList = Var[DirectoryListView](leftDirectoryListView)
  val inactiveList = Var[DirectoryListView](rightDirectoryListView)

}
