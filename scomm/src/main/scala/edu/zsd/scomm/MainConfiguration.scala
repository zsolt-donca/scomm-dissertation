package edu.zsd.scomm

import org.springframework.scala.context.function.FunctionalConfiguration
import edu.zsd.scomm.controller.{MainWindow, DirectoryList}
import edu.zsd.scomm.view.{MainWindowView, DirectoryListView, DirectoriesPaneView}
import edu.zsd.scomm.model.{DirectoriesPaneModel, DirectoryListModel}

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

  val directoriesPaneModel = bean() {
    new DirectoriesPaneModel(leftDirectoryListModel(), rightDirectoryListModel())
  }

  val directoriesPaneView = bean() {
    new DirectoriesPaneView(directoriesPaneModel(), leftDirectoryListView(), rightDirectoryListView())
  }

  val mainWindowView = bean() {
    new MainWindowView(directoriesPaneView())
  }

  val mainWindow = bean() {
    new MainWindow(mainWindowView())
  }
}
