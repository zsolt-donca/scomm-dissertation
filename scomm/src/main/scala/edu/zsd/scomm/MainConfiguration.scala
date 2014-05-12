package edu.zsd.scomm

import org.springframework.scala.context.function.FunctionalConfiguration
import edu.zsd.scomm.controller.{DirectoriesPaneController, MainWindowController, DirectoryListController}
import edu.zsd.scomm.view.{MainWindowView, DirectoryListView, DirectoriesPaneView}
import edu.zsd.scomm.model.{MainWindowModel, DirectoriesPaneModel, DirectoryListModel}

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

  val leftDirectoryListController = bean() {
    new DirectoryListController(leftDirectoryListModel(), leftDirectoryListView())
  }

  val rightDirectoryListModel = bean() {
    new DirectoryListModel(appParams().initRightDir)
  }

  val rightDirectoryListView = bean() {
    new DirectoryListView("directoriesPane.right", rightDirectoryListModel())
  }

  val rightDirectoryListController = bean() {
    new DirectoryListController(rightDirectoryListModel(), rightDirectoryListView())
  }

  val directoriesPaneModel = bean() {
    new DirectoriesPaneModel(leftDirectoryListModel(), rightDirectoryListModel())
  }

  val directoriesPaneView = bean() {
    new DirectoriesPaneView(directoriesPaneModel(), leftDirectoryListView(), rightDirectoryListView())
  }

  val directoriesPaneController = bean() {
    new DirectoriesPaneController(directoriesPaneView(), directoriesPaneModel())
  }

  val mainWindowModel = bean() {
    new MainWindowModel(directoriesPaneModel())
  }

  val mainWindowView = bean() {
    new MainWindowView(mainWindowModel(), directoriesPaneView())
  }

  val mainWindowController = bean() {
    new MainWindowController(mainWindowModel(), mainWindowView())
  }
}
