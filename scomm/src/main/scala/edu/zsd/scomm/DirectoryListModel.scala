package edu.zsd.scomm

import java.io.File
import scala.react.{Observing, Var}

class DirectoryListModel() extends Observing {
  val currentDir: Var[File] = new Var[File](new File(""))
  val currentDirContents: Var[Seq[FileEntry]] = new Var[Seq[FileEntry]](Seq.empty)

  observe(currentDir) {
    currentDir =>
      val listFiles: Seq[File] = currentDir.listFiles()
      val contents: Seq[File] = if (listFiles != null) listFiles else List.empty
      val parentFile: File = if (currentDir.getParentFile != null) currentDir.getParentFile else currentDir
      currentDirContents() = List(FileEntry(parentFile, "..")) ++ contents.map(file => FileEntry(file, file.getName))
      true
  }
}
