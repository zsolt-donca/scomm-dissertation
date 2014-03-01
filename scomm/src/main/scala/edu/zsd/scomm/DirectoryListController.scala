package edu.zsd.scomm

import java.io.File

class DirectoryListController(val initDir: File) extends domain.type#Observing {

  val directoryListView = new DirectoryListView()

  val directoryListModel = new DirectoryListModel()

  observe(directoryListView.selectionEvent) {
    selectionIndex =>
      val files = directoryListModel.currentDirContents()
      val selectedFile: File = files(selectionIndex).file
      println("DirectoryListController: selection event, changing model current dir to: " + selectedFile)
      directoryListModel.currentDir() = selectedFile
      true
  }

  observe(directoryListModel.currentDir) {
    currentDir => directoryListView.updateCurrentDirectory(currentDir); true
  }

  observe(directoryListModel.currentDirContents) {
    contents => directoryListView.updateFiles(contents); true
  }

  directoryListModel.currentDir() = initDir

  override def toString: String = "{Controller " + initDir + "}"
}
