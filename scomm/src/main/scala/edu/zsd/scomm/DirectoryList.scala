package edu.zsd.scomm

import java.io.File
import scala.swing.Reactor
import scala.swing.event.{Key, KeyPressed, ListSelectionChanged, MouseClicked}

/**
 * Controller.
 *
 * @param componentName Swing component name
 * @param initDir initial directory
 */
class DirectoryList(componentName : String, initDir : File) extends Reactor {

  val model = new DirectoryListModel(initDir)
  val view = new DirectoryListView(componentName, model)

  listenTo(view.listView.mouse.clicks, view.listView.selection, view.listView.keys)
  reactions += {
    case MouseClicked(_, _, _, 2, _) => model.enterDirectory << view.listView.selection.leadIndex
    case ListSelectionChanged(_, _, _) => model.selectedIndices() = view.listView.selection.indices.toSet
    case KeyPressed(_, Key.Enter, _, _) => model.enterDirectory << view.listView.selection.leadIndex
    case KeyPressed(_, Key.BackSpace, _, _) => model.goToParent << Unit
  }

}
