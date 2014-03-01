package edu.zsd.scomm

import java.io.File

import edu.zsd.scomm.domain._

class DirectoryListModel {

  val currentDir = domain.Var(new File(""))

  val currentDirContents = Lazy {
    val currentDir: File = DirectoryListModel.this.currentDir()
    println("DirectoryListModel.currentDirContents evaluated with: " + currentDir)
    val listFiles = currentDir.listFiles()
    val contents: Seq[FileEntry] = if (listFiles != null) listFiles.map(file => FileEntry(file, file.getName)) else Seq.empty
    val parentFile: Seq[FileEntry] = if (currentDir.getParentFile != null) Seq(FileEntry(currentDir.getParentFile, "..")) else Seq.empty
    parentFile ++ contents
  }
}
