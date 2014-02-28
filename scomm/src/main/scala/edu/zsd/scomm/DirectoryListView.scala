package edu.zsd.scomm

import scala.swing._
import scala.react.{Observing, Var, EventSource}
import java.io.File
import scala.swing.BorderPanel.Position

class DirectoryListView extends BorderPanel with Observing {

  val selectionEvent = new EventSource[Int]
  val files = new Var[Seq[FileEntry]](Seq.empty)
  val currentDirectory = new Var[File](new File(""))

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

    observe(files) {
      fileSeq => listData = fileSeq.map(file => file.name); true
    }
  }
  add(new ScrollPane(listView), Position.Center)

  observe(currentDirectory) {
    currentDirectory => currentDirectoryLabel.text = currentDirectory.toString; true
  }
}
