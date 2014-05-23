package edu.zsd.scomm.controller

import scala.swing.{ListView, Reactor}
import scala.swing.event.FocusGained
import edu.zsd.scomm.view.DirectoriesPaneView
import edu.zsd.scomm.model.DirectoriesPaneModel
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.typesafe.scalalogging.slf4j.StrictLogging

@Component
class DirectoriesPaneController @Autowired()(val view: DirectoriesPaneView,
                                             val model: DirectoriesPaneModel) extends Reactor with StrictLogging {

  private val leftListView: ListView[String] = view.leftDirectoryListView.listView
  private val rightListView: ListView[String] = view.rightDirectoryListView.listView
  listenTo(leftListView, rightListView)

  reactions += {
    case FocusGained(`leftListView`, Some(`rightListView`), temporary) =>
      logger.debug(s"Focus gained for left list view, temporary: $temporary")
      setFocusTo(left = true)

    case FocusGained(`rightListView`, Some(`leftListView`), temporary) =>
      logger.debug(s"Focus gained for right list view, temporary: $temporary")

      setFocusTo(left = false)
  }

  def setFocusTo(left: Boolean) {
    logger.debug(s"Setting focus to ${if (left) "left" else "right"} list view")

    model.left.active() = left
    model.right.active() = !left
    model.activeListModel() = if (left) view.leftDirectoryListView.model else view.rightDirectoryListView.model
    model.inactiveListModel() = if (left) view.rightDirectoryListView.model else view.leftDirectoryListView.model
    view.activeList() = if (left) view.leftDirectoryListView else view.rightDirectoryListView
    view.inactiveList() = if (left) view.rightDirectoryListView else view.leftDirectoryListView
  }
}
