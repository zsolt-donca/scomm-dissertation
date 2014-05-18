package edu.zsd.scomm.controller

import scala.swing.{ListView, Reactor}
import scala.swing.event.FocusGained
import edu.zsd.scomm.view.DirectoriesPaneView
import edu.zsd.scomm.model.DirectoriesPaneModel
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

@Component
class DirectoriesPaneController @Autowired()(val view: DirectoriesPaneView,
                                             val model: DirectoriesPaneModel) extends Reactor {

  private val leftListView: ListView[String] = view.leftDirectoryListView.listView
  private val rightListView: ListView[String] = view.rightDirectoryListView.listView
  listenTo(leftListView, rightListView)

  reactions += {
    case FocusGained(`leftListView`, _, _) =>
      model.left.active() = true
      model.right.active() = false
      model.activeList() = view.leftDirectoryListView.model
      model.inactiveList() = view.rightDirectoryListView.model
    case FocusGained(`rightListView`, _, _) =>
      model.left.active() = false
      model.right.active() = true
      model.activeList() = view.rightDirectoryListView.model
      model.inactiveList() = view.leftDirectoryListView.model
  }

}
