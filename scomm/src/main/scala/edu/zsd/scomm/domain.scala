package edu.zsd.scomm

import scala.react.Domain
import scala.swing.SimpleSwingApplication
import scala.util.continuations.suspendable

object domain extends Domain {

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

  // TODO investigate how to eliminate the need for the below helper functions

  object suspendable_try {
    def apply(comp: => Unit@suspendable): Unit@suspendable = {
      comp
    }
  }

  object unit {
    def apply() {
    }
  }

}