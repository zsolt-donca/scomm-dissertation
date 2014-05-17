package edu.zsd.scomm.model

import edu.zsd.scomm.domain._
import java.nio.file._
import scala.collection.JavaConverters._

abstract class DirectoryListModel(initDir: Path, diskState: DiskState) extends Observing {

  // basic events
  val processEntry = EventSource[Int]
  val goToParent = EventSource[Unit]
  val selectPaths = EventSource[Seq[Path]]

  // basic variables
  val currentDir = Var[Path](Paths.get(""))
  val selectedIndices = Var[Set[Int]](Set.empty)
  val active = Var[Boolean](false)

  // derived signals
  val currentDirContents: Signal[Seq[FileEntry]] = Strict {
    diskState()
    val currentDir: Path = DirectoryListModel.this.currentDir()
    try {
      val directoryStream: DirectoryStream[Path] = Files.newDirectoryStream(currentDir)
      try {
        val contents: Seq[FileEntry] = directoryStream.asScala.toSeq.map(path => FileEntry(path, path.getFileName.toString)).sortBy(fileEntry => (Files.isRegularFile(fileEntry.path), fileEntry.name.toLowerCase))
        val parentFile: Seq[FileEntry] = if (currentDir.getParent != null) Seq(FileEntry(currentDir.getParent, "..")) else Seq.empty
        parentFile ++ contents
      } finally {
        directoryStream.close()
      }
    } catch {
      case e: AccessDeniedException =>
        e.printStackTrace()
        Seq(FileEntry(currentDir.getParent, ".."))
    }
  }

  val selectedFiles: Signal[Seq[FileEntry]] = Strict {
    val indices = selectedIndices()
    val contents = currentDirContents()

    for ((fileEntry, index) <- contents.zipWithIndex if indices.contains(index)) yield fileEntry
  }

  val selectionInfo: Signal[SelectionInfo] = Strict {
    val selected: Seq[FileEntry] = selectedFiles()
    val size = selected.map(fileEntry => Files.size(fileEntry.path)).sum
    val files = selected.count(fileEntry => Files.isRegularFile(fileEntry.path))
    val directories = selected.count(fileEntry => Files.isDirectory(fileEntry.path))

    SelectionInfo(size, files, directories)
  }

  // observers

  observe(processEntry) {
    index =>
      val previousDir = currentDir.now
      val files = currentDirContents.now
      val selectedPath: Path = files(index).path
      if (Files.isDirectory(selectedPath)) {
        currentDir() = selectedPath
        selectPaths << Seq(previousDir)
      }
  }

  observe(goToParent) {
    _ =>
      val current = currentDir.now
      if (current.getParent != null) {
        processEntry << 0
      }
  }

  observe(selectPaths) {
    selectedPaths =>
      val contents: Seq[FileEntry] = currentDirContents.now
      val indices: Seq[Int] = for ((fileEntry, index) <- contents.zipWithIndex if selectedPaths.contains(fileEntry.path)) yield index

      if (indices.size != selectedPaths.size) {
        println("Error: " + indices + ", " + selectedPaths)
      }
      selectedIndices() = indices.toSet
  }

  observe(active) {
    active =>
      if (!active)
        selectedIndices() = Set.empty
  }

  //  observe(currentDir) {
  //    _ => selectedIndices() = Set.empty
  //  }

  currentDir() = initDir
}
