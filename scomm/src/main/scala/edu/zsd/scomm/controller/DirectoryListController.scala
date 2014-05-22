package edu.zsd.scomm.controller

import scala.swing.Reactor
import scala.swing.event._
import edu.zsd.scomm.model.DirectoryListModel
import edu.zsd.scomm.view.DirectoryListView
import scala.swing.event.KeyPressed
import scala.swing.event.MouseClicked
import com.typesafe.scalalogging.slf4j.StrictLogging

/**
 * Controller.
 *
 */
abstract class DirectoryListController (model : DirectoryListModel,
                                        view: DirectoryListView) extends Reactor with StrictLogging {

  listenTo(view.listView.mouse.clicks, view.listView.selection, view.listView.keys)
  reactions += {
    case MouseClicked(_, _, _, 2, _) =>
      val leadIndex = view.listView.selection.leadIndex
      logger.debug(s"Double click, going to $leadIndex")
      model.goToIndex << leadIndex
    case ListSelectionChanged(_, _, false) =>
      if (!view.listView.updating) {
        val selection = view.listView.selection.indices.toSet
        logger.debug(s"List selection changed, setting model indices to $selection")
        model.selectIndices << selection
      }
    case KeyPressed(_, Key.Enter, _, _) =>
      val leadIndex = view.listView.selection.leadIndex
      logger.debug(s"Enter pressed, going to $leadIndex")
      model.goToIndex << leadIndex
    case KeyPressed(_, Key.BackSpace, _, _) =>
      logger.debug("Backspace pressed, going to parent")
      model.goToParent << Unit
  }
}
