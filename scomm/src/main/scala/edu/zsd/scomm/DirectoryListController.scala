package edu.zsd.scomm

import java.io.File
import edu.zsd.scomm.domain._

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

  observe(directoryListView.selectedIndices) {
    selectedIndices =>

  }

  observe(directoryListModel.currentDir) {
    currentDir => directoryListView.updateCurrentDirectory(currentDir); true
  }

  observe(directoryListModel.currentDirContents) {
    contents => directoryListView.updateFiles(contents); true
  }

  directoryListModel.currentDir() = initDir
}
