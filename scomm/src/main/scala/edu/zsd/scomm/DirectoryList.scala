package edu.zsd.scomm

import scala.swing._
import edu.zsd.scomm.domain._
import scala.swing.BorderPanel.Position
import scala.swing.event.{Key, KeyPressed, ListSelectionChanged, MouseClicked}
import java.io.File

class DirectoryList(componentName : String, initDir: File) extends BorderPanel with Observing {

  name = componentName

  // basic events and signals
  val enterDirectory = EventSource[Int]
  val goToParent = EventSource[Unit]

  val selectedIndices = Var[Set[Int]](Set.empty)
  val currentDir = Var(new File(""))
  // derived events and signals

  val currentDirContents: Signal[Seq[FileEntry]] = Strict {
    val currentDir: File = DirectoryList.this.currentDir()
    val listFiles = currentDir.listFiles()
    val contents: Seq[FileEntry] = if (listFiles != null) listFiles.map(file => FileEntry(file, file.getName)).sortBy(fileEntry => (fileEntry.file.isFile, fileEntry.name.toLowerCase)) else Seq.empty
    val parentFile: Seq[FileEntry] = if (currentDir.getParentFile != null) Seq(FileEntry(currentDir.getParentFile, "..")) else Seq.empty
    parentFile ++ contents
  }

  // TODO investigate why it fails if this is Strict
  val selectedFiles: Signal[Seq[FileEntry]] = Lazy {
    val indices = selectedIndices()
    val contents = currentDirContents()

    for ((fileEntry, index) <- contents.zipWithIndex if indices.contains(index)) yield fileEntry
  }

  case class SelectionInfo(size: Long, selectedFiles: Int, selectedFolders: Int)

  val selectionInfo: Signal[SelectionInfo] = Strict {
    val selected: Seq[FileEntry] = selectedFiles()
    val size = selected.map(fileEntry => fileEntry.file.length).sum
    val files = selected.count(fileEntry => fileEntry.file.isFile)
    val directories = selected.count(fileEntry => fileEntry.file.isDirectory)

    SelectionInfo(size, files, directories)
  }

  val goToParentLoop = Reactor.loop { self =>
    self awaitNext goToParent
    val current = currentDir.now
    if (current.getParentFile != null) {
      enterDirectory << 0
    }
  }

  currentDir() = initDir


  // components

  private[this] val currentDirLabel = new Label("<empty>")
  currentDirLabel.name = componentName + ".currentDirLabel"
  currentDirLabel.horizontalAlignment = Alignment.Left
  add(currentDirLabel, Position.North)

  val listView = new ListView[String]() {
    listenTo(mouse.clicks, selection, keys)
    reactions += {
      case MouseClicked(_, _, _, 2, _) => enterDirectory << selection.leadIndex
      case ListSelectionChanged(_, _, _) => selectedIndices() = selection.indices.toSet
      case KeyPressed(_, Key.Enter, _, _) => enterDirectory << selection.leadIndex
      case KeyPressed(_, Key.BackSpace, _, _) => goToParent << Unit
    }
  }
  listView.name = componentName + ".listView"
  add(new ScrollPane(listView), Position.Center)

  private[this] val summaryLabel = new Label("<empty>")
  summaryLabel.name = componentName + ".summaryLabel"
  summaryLabel.horizontalAlignment = Alignment.Left
  add(summaryLabel, Position.South)

  // scala-react code related to the components

  observe(enterDirectory) {
    index =>
      val files = currentDirContents.now
      val selectedFile: File = files(index).file
      currentDir() = selectedFile
      listView.selection.indices.clear()
  }

  observe(currentDir) {
    currentDir => currentDirLabel.text = currentDir.toString
  }

  observe(currentDirContents) {
    contents => listView.listData = contents.map(file => file.name)
  }

  observe(selectionInfo) {
    info => summaryLabel.text = s"${info.size} bytes, ${info.selectedFiles} file(s), ${info.selectedFolders} folder(s)"
  }

}
