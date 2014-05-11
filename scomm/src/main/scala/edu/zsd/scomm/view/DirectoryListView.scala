package edu.zsd.scomm.view

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import edu.zsd.scomm.model.DirectoryListModel
import java.awt.Color

class DirectoryListView(componentName : String, model : DirectoryListModel) extends BorderPanel with Observing {

  name = componentName

  private[this] val currentDirLabel = new Label("<empty>")
  currentDirLabel.name = componentName + ".currentDirLabel"
  currentDirLabel.horizontalAlignment = Alignment.Left

  private[this] val currentDirPanel = new BorderPanel() {
    add(currentDirLabel, Position.Center)
  }
  add(currentDirPanel, Position.North)

  val listView = new ListView[String]()
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
      listView.selection.indices.clear()
  }

  observe(model.currentDirContents) {
    contents => listView.listData = contents.map(file => file.name)
  }

  observe(model.selectionInfo) {
    info => summaryLabel.text = s"${info.size} bytes, ${info.selectedFiles} file(s), ${info.selectedFolders} folder(s)"
  }

  observe(model.selectedIndices) {
    selectedIndices : Set[Int] =>
      listView.selection.indices.clear()
      listView.selection.indices ++= selectedIndices
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
