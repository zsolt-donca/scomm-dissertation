package edu.zsd.scomm

import scala.swing._
import scala.react.{Observing, EventSource}
import java.io.File
import scala.swing.BorderPanel.Position

class DirectoryListView extends BorderPanel with Observing {

  val selectionEvent = new EventSource[Int]

  private[this] val currentDirectoryLabel = new Label("<empty>")
  currentDirectoryLabel.horizontalAlignment = Alignment.Left
  add(currentDirectoryLabel, Position.North)

  private[this] val listView = new ListView[String]() {
    listenTo(mouse.clicks)
    reactions += {
      case scala.swing.event.MouseClicked(_, _, _, 2, _) =>
        val selectedIndices: Array[Int] = selection.indices.toArray
        if (selectedIndices.nonEmpty) {
          selectionEvent.emit(selectedIndices(0))
        }
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
