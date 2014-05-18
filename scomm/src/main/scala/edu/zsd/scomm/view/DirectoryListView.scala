package edu.zsd.scomm.view

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import edu.zsd.scomm.model.{FileEntry, DirectoryListModel}
import java.awt.Color
import java.nio.file.Path

abstract class DirectoryListView(componentName: String, model: DirectoryListModel) extends BorderPanel with Observing {

  name = componentName

  private[this] val currentDirLabel = new Label("<empty>")
  currentDirLabel.name = componentName + ".currentDirLabel"
  currentDirLabel.horizontalAlignment = Alignment.Left

  private[this] val currentDirPanel = new BorderPanel() {
    add(currentDirLabel, Position.Center)
  }
  add(currentDirPanel, Position.North)

  //  val listView = new FilesListView
  val listView = new ListView[String] {
    var updating = false
  }
  listView.name = componentName + ".listView"
  add(new ScrollPane(listView), Position.Center)

  private[this] val summaryLabel = new Label("<empty>")
  summaryLabel.name = componentName + ".summaryLabel"
  summaryLabel.horizontalAlignment = Alignment.Left
  add(summaryLabel, Position.South)

  // scala.react code related to the components

  observe(model.currentDir) {
    currentDir =>
      currentDirLabel.text = currentDir.toString
  }

  observe(model.currentDirContents) {
    contents =>
      try {
        listView.updating = true
        val selection = listView.selection.indices.toSeq
        listView.listData = contents.map(file => file.name)
        listView.selection.indices.clear()
        listView.selection.indices ++= selection
      } finally {
        listView.updating = false
      }
  }

  observe(model.selectionInfo) {
    info => summaryLabel.text = s"${info.size} bytes, ${info.files} file(s), ${info.folders} folder(s)"
  }

  observe(model.selectedPaths) {
    selectedPaths: Set[Path] =>

      val currentDirContents: Seq[FileEntry] = model.currentDirContents.now
      val selectedIndices: Set[Int] = selectedPaths.map(path => currentDirContents.indexWhere(fileEntry => fileEntry.path == path))

      if (listView.selection.indices.toSet != selectedIndices) {
        try {
          listView.updating = true
          listView.selection.indices.clear()
          listView.selection.indices ++= selectedIndices
        } finally {
          listView.updating = false
        }
        if (selectedIndices.nonEmpty) {
          listView.ensureIndexIsVisible(selectedIndices.toSeq(0))
        }
      }
  }

  observe(model.active) {
    active =>
      if (active) {
        currentDirPanel.background = Color.BLUE
        currentDirLabel.foreground = Color.WHITE
      } else {
        currentDirPanel.background = Color.LIGHT_GRAY
        currentDirLabel.foreground = Color.BLACK
      }
  }
}
