package edu.zsd.scomm.controller

import scala.swing.Reactor
import scala.swing.event.{Key, KeyPressed, ListSelectionChanged, MouseClicked}
import edu.zsd.scomm.model.DirectoryListModel
import edu.zsd.scomm.view.DirectoryListView

/**
 * Controller.
 *
 */
class DirectoryList(val model : DirectoryListModel, val view : DirectoryListView) extends Reactor {

  listenTo(view.listView.mouse.clicks, view.listView.selection, view.listView.keys)
  reactions += {
    case MouseClicked(_, _, _, 2, _) => model.enterDirectory << view.listView.selection.leadIndex
    case ListSelectionChanged(_, _, _) => model.selectedIndices() = view.listView.selection.indices.toSet
    case KeyPressed(_, Key.Enter, _, _) => model.enterDirectory << view.listView.selection.leadIndex
    case KeyPressed(_, Key.BackSpace, _, _) => model.goToParent << Unit
  }

}
