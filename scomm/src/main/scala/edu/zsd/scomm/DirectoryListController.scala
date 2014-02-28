package edu.zsd.scomm

import java.io.File
import scala.react.Observing

class DirectoryListController(val initDir: File) extends Observing {

  val directoryListView = new DirectoryListView()

  val directoryListModel = new DirectoryListModel()

  observe(directoryListView.selectionEvent) {
    selectionIndex =>
      val files = directoryListModel.currentDirContents()
      val selectedFile: File = files(selectionIndex).file
      directoryListModel.currentDir() = selectedFile
      true
  }

  // TODO this might be buggy sometimes
  observe(directoryListModel.currentDir) {
    currentDir =>
      directoryListView.currentDirectory() = currentDir
      true
  }

  observe(directoryListModel.currentDirContents) {
    currentDirContents =>
      directoryListView.files() = currentDirContents
      true
  }

  directoryListModel.currentDir() = initDir
}
