package edu.zsd.scomm.controller

import scala.swing.Reactor
import scala.swing.event._
import edu.zsd.scomm.model.DirectoryListModel
import edu.zsd.scomm.view.DirectoryListView
import scala.swing.event.KeyPressed
import scala.swing.event.MouseClicked

/**
 * Controller.
 *
 */
abstract class DirectoryListController (model : DirectoryListModel,
                                        view : DirectoryListView) extends Reactor {

  listenTo(view.listView.mouse.clicks, view.listView.selection, view.listView.keys)
  reactions += {
    case MouseClicked(_, _, _, 2, _) => model.goToIndex << view.listView.selection.leadIndex
    case ListSelectionChanged(_, _, _) =>
      if (!view.listView.updating) {
        model.selectIndices << view.listView.selection.indices.toSet
      }
    case KeyPressed(_, Key.Enter, _, _) => model.goToIndex << view.listView.selection.leadIndex
    case KeyPressed(_, Key.BackSpace, _, _) => model.goToParent << Unit
  }
}
