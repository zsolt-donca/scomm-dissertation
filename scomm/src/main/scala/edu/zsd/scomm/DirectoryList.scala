package edu.zsd.scomm

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import scala.swing.event.{Key, KeyPressed, ListSelectionChanged, MouseClicked}
import java.io.File

class DirectoryList(initDir: File) extends BorderPanel with Observing {

  val enterDirectory = EventSource[Int]
  val selectedIndices = Var[Set[Int]](Set.empty)

  private[this] val currentDirLabel = new Label("<empty>")
  currentDirLabel.horizontalAlignment = Alignment.Left
  add(currentDirLabel, Position.North)

  private[this] val listView = new ListView[String]() {
    listenTo(mouse.clicks, selection, keys)
    reactions += {
      case MouseClicked(_, _, _, 2, _) => enterDirectory << selection.leadIndex
      case ListSelectionChanged(_, _, _) => selectedIndices() = selection.indices.toSet
      case KeyPressed(_, Key.Enter, _, _) => enterDirectory << selection.leadIndex
    }
  }
  add(new ScrollPane(listView), Position.Center)

  val currentDir = Var(new File(""))

  val currentDirContents: Signal[Seq[FileEntry]] = Strict {
    val currentDir: File = DirectoryList.this.currentDir()
    val listFiles = currentDir.listFiles()
    val contents: Seq[FileEntry] = if (listFiles != null) listFiles.map(file => FileEntry(file, file.getName)) else Seq.empty
    val parentFile: Seq[FileEntry] = if (currentDir.getParentFile != null) Seq(FileEntry(currentDir.getParentFile, "..")) else Seq.empty
    parentFile ++ contents
  }

  val selectedFiles: Signal[Seq[FileEntry]] = Lazy {
    val indices = selectedIndices()
    val contents = currentDirContents()

    for ((fileEntry, index) <- contents.zipWithIndex if indices.contains(index)) yield fileEntry
  }

  observe(enterDirectory) {
    index =>
      val files = currentDirContents.now
      val selectedFile: File = files(index).file
      currentDir() = selectedFile
      listView.selection.indices.clear()
      true
  }

  observe(currentDir) {
    currentDir => currentDirLabel.text = currentDir.toString; true
  }

  observe(currentDirContents) {
    contents => listView.listData = contents.map(file => file.name); true
  }

  currentDir() = initDir

}
