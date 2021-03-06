package edu.zsd.scomm

import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}

class MainConfiguration extends FunctionalConfiguration with ContextSupport {

  val appParams = bean("appParams") {
    edu.zsd.scomm.Main.appParams
  }

  componentScan("edu.zsd.scomm")

}
