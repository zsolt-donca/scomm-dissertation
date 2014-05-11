package edu.zsd.scomm.view

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import edu.zsd.scomm.model.DirectoryListModel

class DirectoryListView(componentName : String, model : DirectoryListModel) extends BorderPanel with Observing {

  name = componentName

  private[this] val currentDirLabel = new Label("<empty>")
  currentDirLabel.name = componentName + ".currentDirLabel"
  currentDirLabel.horizontalAlignment = Alignment.Left
  add(currentDirLabel, Position.North)

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

}
