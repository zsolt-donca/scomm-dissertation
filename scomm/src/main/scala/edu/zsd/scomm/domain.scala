package edu.zsd.scomm

import scala.react.Domain
import scala.swing.SimpleSwingApplication
import scala.util.continuations.{cpsParam, suspendable}
import scala.collection.IterableLike

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

  object suspendable_block {
    def apply(comp: => Unit@suspendable): Unit@suspendable = {
      comp
    }
  }

  object unit {
    def apply() {
    }
  }

  implicit def cpsIterable[A, Repr](xs: IterableLike[A, Repr]) = new {
    def cps = new {
      def foreach[B](f: A => Any@cpsParam[Unit, Unit]): Unit@cpsParam[Unit, Unit] = {
        val it = xs.iterator
        while (it.hasNext) f(it.next())
      }
    }
  }

}