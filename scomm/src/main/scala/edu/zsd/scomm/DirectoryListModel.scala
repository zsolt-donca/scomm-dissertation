package edu.zsd.scomm

import java.io.File
import edu.zsd.scomm.domain._

class DirectoryListModel(initDir : File) extends Observing {

  // basic events and signals
  val enterDirectory = EventSource[Int]
  val goToParent = EventSource[Unit]

  val selectedIndices = Var[Set[Int]](Set.empty)
  val currentDir = Var(new File(""))

  // derived events and signals
  val currentDirContents: Signal[Seq[FileEntry]] = Strict {
    val currentDir: File = DirectoryListModel.this.currentDir()
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

  val selectionInfo: Signal[SelectionInfo] = Strict {
    val selected: Seq[FileEntry] = selectedFiles()
    val size = selected.map(fileEntry => fileEntry.file.length).sum
    val files = selected.count(fileEntry => fileEntry.file.isFile)
    val directories = selected.count(fileEntry => fileEntry.file.isDirectory)

    SelectionInfo(size, files, directories)
  }

  observe(enterDirectory) {
    index =>
      val files = currentDirContents.now
      val selectedFile: File = files(index).file
      currentDir() = selectedFile
  }

  observe(goToParent) {
    _ =>
      val current = currentDir.now
      if (current.getParentFile != null) {
        enterDirectory << 0
      }
  }

  currentDir() = initDir

}

case class SelectionInfo(size: Long, selectedFiles: Int, selectedFolders: Int)

case class FileEntry(file: File, name: String)
