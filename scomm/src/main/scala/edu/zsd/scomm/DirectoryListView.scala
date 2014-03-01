package edu.zsd.scomm

import scala.swing._
import java.io.File
import scala.swing.BorderPanel.Position
import edu.zsd.scomm.domain._
import scala.swing.event.{MouseClicked, ListSelectionChanged}
import javax.swing.JList

class DirectoryListView() extends BorderPanel with Observing {

  val selectionEvent = EventSource[Int]
  val selectedIndices = Var[Set[Int]](Set.empty)

  private[this] val currentDirectoryLabel = new Label("<empty>")
  currentDirectoryLabel.horizontalAlignment = Alignment.Left
  add(currentDirectoryLabel, Position.North)

  private[this] val listView = new ListView[String]() {

    listenTo(mouse.clicks, selection)
    reactions += {
      case MouseClicked(_, _, _, 2, _) => selectionEvent << selection.leadIndex
      case ListSelectionChanged(_, _, _) => selectedIndices() = selection.indices.toSet
    }
  }

  add(new ScrollPane(listView), Position.Center)

  def updateFiles(files: Seq[FileEntry]): Unit = {
    listView.listData = files.map(file => file.name)
  }

  def updateCurrentDirectory(currentDirectory: File): Unit = {
    currentDirectoryLabel.text = currentDirectory.toString
  }
}

