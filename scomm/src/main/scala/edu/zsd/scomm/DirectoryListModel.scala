package edu.zsd.scomm

import java.io.File
import scala.react.{Signal, CachedSignal, Observing, Var}

class DirectoryListModel() extends Observing {
  val currentDir: Var[File] = new Var[File](new File("")) {
    override def toString: String = "DirectoryListModel.currentDir"
  }

  val currentDirContents: Signal[Seq[FileEntry]] = new CachedSignal({
    val currentDir: File = DirectoryListModel.this.currentDir()
    println("DirectoryListModel.currentDirContents evaluated with: " + currentDir)
    val listFiles = currentDir.listFiles()
    val contents: Seq[FileEntry] = if (listFiles != null) listFiles.map(file => FileEntry(file, file.getName)) else Seq.empty
    val parentFile: Seq[FileEntry] = if (currentDir.getParentFile != null) Seq(FileEntry(currentDir.getParentFile, "..")) else Seq.empty
    parentFile ++ contents
  })
}
