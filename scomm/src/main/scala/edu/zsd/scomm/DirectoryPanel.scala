package edu.zsd.scomm

import scala.swing.{Alignment, BorderPanel, ScrollPane, Label}
import scala.swing.BorderPanel.Position
import java.io.File
import scala.react.Observing

class DirectoryPanel(val initialDirectory : File) extends BorderPanel with Observing {

  val actualDirectory = new Label(initialDirectory.toString)
  actualDirectory.horizontalAlignment = Alignment.Left
  add(actualDirectory, Position.North)

  val listView: DirectoryListView = new DirectoryListView(initialDirectory)
  add(new ScrollPane(listView), Position.Center)
  listenTo(listView)

  observe(listView.currentDir) { x => actualDirectory.text = x.toString; true }
}
