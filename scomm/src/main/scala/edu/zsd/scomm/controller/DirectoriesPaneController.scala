package edu.zsd.scomm.controller

import scala.swing.{ListView, Reactor}
import scala.swing.event.{FocusLost, FocusGained}
import edu.zsd.scomm.view.DirectoriesPaneView

class DirectoriesPaneController(val view : DirectoriesPaneView) extends Reactor {

  private val leftListView: ListView[String] = view.leftDirectoryListView.listView
  private val rightListView: ListView[String] = view.rightDirectoryListView.listView
  listenTo(leftListView, rightListView)

  reactions += {
    case FocusGained(`leftListView`, _, _) =>
      view.model.left.active() = true
    case FocusGained(`rightListView`, _, _) =>
      view.model.right.active() = true

    case FocusLost(`leftListView`, Some(`rightListView`), _) =>
      view.model.left.active() = false
    case FocusLost(`rightListView`, Some(`leftListView`), _) =>
      view.model.right.active() = false
  }

}
