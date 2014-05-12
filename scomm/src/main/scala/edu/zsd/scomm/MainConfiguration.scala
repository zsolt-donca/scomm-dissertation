package edu.zsd.scomm

import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import edu.zsd.scomm.controller.MainWindowController
import edu.zsd.scomm.view.DirectoriesPaneView
import edu.zsd.scomm.model.DirectoriesPaneModel
import org.springframework.context.annotation.{Bean, Configuration, ComponentScan}

class MainConfiguration extends FunctionalConfiguration with ContextSupport {

  val appParams = bean("appParams") {
    edu.zsd.scomm.main.appParams
  }

  componentScan("edu.zsd.scomm")

}
