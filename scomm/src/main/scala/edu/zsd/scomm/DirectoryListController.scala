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
      println("DirectoryListController: selection event, changing model current dir to: " + selectedFile)
      directoryListModel.currentDir() = selectedFile
      true
  }

  observe(directoryListModel.currentDir) {
    currentDir =>
      println("DirectoryListController: model.currentDir changed to " + currentDir)
      directoryListView.updateCurrentDirectory(currentDir)
      true
  }

  observe(directoryListModel.currentDirContents) {
    currentDirContents =>
      println("DirectoryListController: model.currentDirContents changed to " + currentDirContents)
      directoryListView.updateFiles(currentDirContents)
      true
  }

  directoryListModel.currentDir() = initDir

  override def toString: String = "{Controller " + initDir + "}"
}
