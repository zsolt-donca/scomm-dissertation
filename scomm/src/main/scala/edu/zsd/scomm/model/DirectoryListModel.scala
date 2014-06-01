package edu.zsd.scomm.model

import edu.zsd.scomm.Domain._
import java.nio.file._
import edu.zsd.scomm.Utils.directoryList
import com.typesafe.scalalogging.slf4j.StrictLogging

abstract class DirectoryListModel(initDir: Path, diskState: DiskState) extends Observing with StrictLogging {

  // basic events
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
      val list = directoryList(currentDir)
      logger.debug(s"Calculating currentDirContents, currentDir: $currentDir, list: $list")
      val contents: Seq[FileEntry] = list.map(path => FileEntry(path, path.getFileName.toString)).sortBy(fileEntry => (Files.isRegularFile(fileEntry.path), fileEntry.name.toLowerCase))
      val parentFile: Seq[FileEntry] = if (currentDir.getParent != null) Seq(FileEntry(currentDir.getParent, "..")) else Seq.empty
      parentFile ++ contents
    } catch {
      case e: AccessDeniedException =>
        logger.error(e.toString, e)
        Seq(FileEntry(currentDir.getParent, ".."))
    }
  }

  val selectionInfo: Signal[SelectionInfo] = Strict {

    val currentDir = this.currentDir()
    val selected: Set[Path] = selectedPaths()

    val paths = if (currentDir.getParent != null && selected(currentDir.getParent))
      selected - currentDir.getParent
    else selected

    val size = paths.map(path => Files.size(path)).sum
    val files = paths.count(path => Files.isRegularFile(path))
    val directories = paths.count(path => Files.isDirectory(path))

    SelectionInfo(size, files, directories, paths)
  }

  // observers

  observe(goToParent) {
    _ =>
      val parent = currentDir.now.getParent
      if (parent != null) {
        processPath(parent)
      }
  }

  observe(goToIndex) {
    index => processPath(currentDirContents.now(index).path)
  }

  observe(selectIndices) {
    indices =>
      val contents = currentDirContents.now
      val paths: Set[Path] = indices.map(index => contents(index).path)
      selectedPaths() = paths
  }

  observe(active) {
    active => if (!active) selectedPaths() = Set.empty
  }

  currentDir() = initDir

  private def processPath(path: Path) {
    val previousDir = currentDir.now
    if (Files.isDirectory(path)) {
      currentDir() = path
      selectedPaths() = Set(previousDir)
    }
  }
}
