package edu.zsd.scomm

import scala.swing.{Alignment, BorderPanel, ScrollPane, Label}
import scala.swing.BorderPanel.Position
import java.io.File

class DirectoryPanel(val initialDirectory : File) extends BorderPanel {

  val actualDirectory = new Label(initialDirectory.toString)
  actualDirectory.horizontalAlignment = Alignment.Left
  add(actualDirectory, Position.North)

  val listView: DirectoryListView = new DirectoryListView(initialDirectory)
  add(new ScrollPane(listView), Position.Center)
  listenTo(listView)

  reactions += {
    case listView.currentDirChangedEvent => actualDirectory.text = listView.currentDir.toString
  }
}
