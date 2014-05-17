package edu.zsd.scomm.model

import edu.zsd.scomm.domain._
import java.nio.file._
import scala.collection.JavaConverters._

abstract class DirectoryListModel(initDir: Path, diskState: DiskState) extends Observing {

  // basic events
  val processPath = EventSource[Path]
  val goToParent = EventSource[Unit]
  val goToIndex = EventSource[Int]
  val selectIndices = EventSource[Set[Int]]

  // basic variables
  val currentDir = Var[Path](Paths.get(""))
  val selectedPaths = Var[Set[Path]](Set.empty)
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

  val selectionInfo: Signal[SelectionInfo] = Strict {
    val selected: Set[Path] = selectedPaths()
    val size = selected.map(path => Files.size(path)).sum
    val files = selected.count(path => Files.isRegularFile(path))
    val directories = selected.count(path => Files.isDirectory(path))

    SelectionInfo(size, files, directories)
  }

  // observers

  observe(processPath) {
    path =>
      val previousDir = currentDir.now
      if (Files.isDirectory(path)) {
        currentDir() = path
        selectedPaths() = Set(previousDir)
      }
  }

  observe(goToParent) {
    _ =>
      val parent = currentDir.now.getParent
      if (parent != null) {
        processPath << parent
      }
  }

  observe(goToIndex) {
    index => processPath << currentDirContents.now(index).path
  }

  observe(selectIndices) {
    indices =>
      val contents = currentDirContents.now
      selectedPaths() = indices.map(index => contents(index).path)
  }

  observe(active) {
    active => if (!active) selectedPaths() = Set.empty
  }

  currentDir() = initDir
}
