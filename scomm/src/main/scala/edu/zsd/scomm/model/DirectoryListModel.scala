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
  val currentDirectory = Var[Path](Paths.get(""))
  val selectedPaths = Var[Set[Path]](Set.empty)
  val active = Var[Boolean](false)

  // derived signals
  val currentDirContents: Signal[Seq[FileEntry]] = Strict {
    diskState()
    val currentDir: Path = currentDirectory()
    try {
      val list = directoryList(currentDir)
      logger.debug(s"Calculating currentDirContents, currentDir: $currentDir, list: $list")
      val contents: Seq[FileEntry] = list.filter(path => !Files.isHidden(path))
        .map(path => FileEntry(path, path.getFileName.toString))
        .sortBy(fileEntry => (Files.isRegularFile(fileEntry.path), fileEntry.name.toLowerCase))
      val parentFile: Seq[FileEntry] = if (currentDir.getParent != null) Seq(FileEntry(currentDir.getParent, "..")) else Seq.empty
      parentFile ++ contents
    } catch {
      case e: AccessDeniedException =>
        logger.error(e.toString, e)
        Seq(FileEntry(currentDir.getParent, ".."))
    }
  }

  val selectionInfo: Signal[SelectionInfo] = Strict {

    val currentDir = this.currentDirectory()
    val selected: Set[Path] = selectedPaths()

    val paths = if (currentDir.getParent != null && selected(currentDir.getParent))
      selected - currentDir.getParent
    else selected

    val size = paths.filter(Files.isRegularFile(_)).map(Files.size).sum
    val files = paths.count(Files.isRegularFile(_))
    val directories = paths.count(Files.isDirectory(_))

    SelectionInfo(size, files, directories, paths)
  }

  // observers

  val goToParentReactor = Reactor.loop {
    self =>
      self awaitNext goToParent
      val parent = currentDirectory.now.getParent
      if (parent != null) {
        processPath(parent)
      }
  }

  val goToIndexReactor = Reactor.loop {
    self =>
      val index = self awaitNext goToIndex
      processPath(currentDirContents.now(index).path)
  }

  val selectIndicesReactor = Reactor.loop {
    self =>
      val indices = self awaitNext selectIndices
      val contents = currentDirContents.now
      val paths: Set[Path] = indices.map(index => contents(index).path)
      selectedPaths() = paths
  }

  val activeReactor = Reactor.loop {
    self =>
      if (!(self awaitNext active))
        selectedPaths() = Set.empty
  }

  currentDirectory() = initDir

  private def processPath(path: Path) {
    val previousDir = currentDirectory.now
    if (Files.isDirectory(path)) {
      currentDirectory() = path
      selectedPaths() = Set(previousDir)
    }
  }
}
