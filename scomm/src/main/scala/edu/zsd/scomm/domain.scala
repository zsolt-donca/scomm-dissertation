package edu.zsd.scomm

import scala.react.Domain
import scala.swing.SimpleSwingApplication

object domain extends SwingDomain {

  self: Domain =>

  val scheduler = new SwingScheduler()
  val engine = new Engine
}

abstract class SwingDomain extends Domain {

  trait ReactiveSwingApp extends SimpleSwingApplication {
    override def main(args: Array[String]) {
      schedule {
        startup(args)
      }
      start()
    }
  }

}