package edu.zsd.scomm

import scala.swing.SimpleSwingApplication
import scala.util.continuations.suspendable

object Domain extends scala.react.Domain {

  val scheduler = new SwingScheduler()
  val engine = new Engine

  trait ReactiveSimpleSwingApplication extends SimpleSwingApplication {
    override def main(args: Array[String]) {
      schedule {
        startup(args)
      }
      start()
    }
  }

}