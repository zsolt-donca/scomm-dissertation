package edu.zsd.scomm.model

import edu.zsd.scomm.domain._
import java.nio.file._
import scala.collection.JavaConverters._

abstract class DirectoryListModel(initDir: Path, diskState: DiskState) extends Observing {

  // basic events and signals
  val processEntry = EventSource[Int]
  val goToParent = EventSource[Unit]

  val active = Var[Boolean](false)
  val selectedIndices = Var[Set[Int]](Set.empty)
  val currentDir = Var(Paths.get(""))

  // derived events and signals
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
      case e : AccessDeniedException =>
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

  observe(processEntry) {
    index =>
      val files = currentDirContents.now
      val selectedPath: Path = files(index).path
      if (Files.isDirectory(selectedPath)) {
        currentDir() = selectedPath
      }
  }

  observe(goToParent) {
    _ =>
      val current = currentDir.now
      if (current.getParent != null) {
        processEntry << 0
      }
  }

  observe(active) {
    active =>
      if (!active)
        selectedIndices() = Set.empty
  }

  currentDir() = initDir
}
