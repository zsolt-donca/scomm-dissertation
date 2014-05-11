package edu.zsd.scomm.model

import edu.zsd.scomm.domain._
import java.nio.file._
import scala.collection.JavaConverters._

class DirectoryListModel(initDir : Path) extends Observing {

  // basic events and signals
  val enterDirectory = EventSource[Int]
  val goToParent = EventSource[Unit]

  val active = Var[Boolean](false)
  val selectedIndices = Var[Set[Int]](Set.empty)
  val currentDir = Var(Paths.get(""))

  // derived events and signals
  val currentDirContents: Signal[Seq[FileEntry]] = Strict {
    val currentDir: Path = DirectoryListModel.this.currentDir()
    val directoryStream: DirectoryStream[Path] = Files.newDirectoryStream(currentDir)
    try {
      val contents: Seq[FileEntry] = directoryStream.asScala.toSeq.map(path => FileEntry(path, path.getFileName.toString)).sortBy(fileEntry => (Files.isRegularFile(fileEntry.path), fileEntry.name.toLowerCase))
      val parentFile: Seq[FileEntry] = if (currentDir.getParent != null) Seq(FileEntry(currentDir.getParent, "..")) else Seq.empty
      parentFile ++ contents
    } finally {
      directoryStream.close()
    }
  }

  // TODO investigate why it fails if this is Strict
  val selectedFiles: Signal[Seq[FileEntry]] = Lazy {
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

  observe(enterDirectory) {
    index =>
      val files = currentDirContents.now
      val selectedPath: Path = files(index).path
      currentDir() = selectedPath
  }

  observe(goToParent) {
    _ =>
      val current = currentDir.now
      if (current.getParent != null) {
        enterDirectory << 0
      }
  }

  observe(active) {
    active =>
      if (!active)
        selectedIndices() = Set.empty
  }

  currentDir() = initDir
}
