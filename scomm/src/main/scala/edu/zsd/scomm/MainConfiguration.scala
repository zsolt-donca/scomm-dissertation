package edu.zsd.scomm

import org.springframework.scala.context.function.FunctionalConfiguration
import edu.zsd.scomm.controller.{DirectoryList, MainWindow}
import edu.zsd.scomm.view.{DirectoryListView, DirectoriesPaneView}
import edu.zsd.scomm.model.DirectoryListModel

class MainConfiguration extends FunctionalConfiguration {

  val appParams = bean("appParams") {
    edu.zsd.scomm.main.appParams
  }

  val leftDirectoryListModel = bean() {
    new DirectoryListModel(appParams().initLeftDir)
  }

  val leftDirectoryListView = bean() {
    new DirectoryListView("directoriesPane.left", leftDirectoryListModel())
  }

  val leftDirectoryList = bean() {
    new DirectoryList(leftDirectoryListModel(), leftDirectoryListView())
  }

  val rightDirectoryListModel = bean() {
    new DirectoryListModel(appParams().initRightDir)
  }

  val rightDirectoryListView = bean() {
    new DirectoryListView("directoriesPane.right", rightDirectoryListModel())
  }

  val rightDirectoryList = bean() {
    new DirectoryList(rightDirectoryListModel(), rightDirectoryListView())
  }

  val directoriesPane = bean() {
    new DirectoriesPaneView(leftDirectoryList(), rightDirectoryList())
  }

  val mainWindow = bean("mainWindow") {
    new MainWindow(directoriesPane())
  }
}
