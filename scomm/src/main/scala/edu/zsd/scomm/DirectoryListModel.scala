package edu.zsd.scomm

import java.io.File
import scala.react.{CachedSignal, Observing, Var}

class DirectoryListModel() extends Observing {
  val currentDir: Var[File] = new Var[File](new File("")) {
    override def toString: String = "DirectoryListModel.currentDir"
  }

  val currentDirContents = new CachedSignal({
    val currentDir: File = DirectoryListModel.this.currentDir()
    println("DirectoryListModel.currentDirContents evaluated with: " + currentDir)
    val listFiles: Seq[File] = currentDir.listFiles()
      val contents: Seq[File] = if (listFiles != null) listFiles else List.empty
      val parentFile: File = if (currentDir.getParentFile != null) currentDir.getParentFile else currentDir
    val result: Seq[FileEntry] = List(FileEntry(parentFile, "..")) ++ contents.map(file => FileEntry(file, file.getName))
    result
  })
}
