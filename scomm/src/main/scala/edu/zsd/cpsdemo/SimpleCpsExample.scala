package edu.zsd.cpsdemo

import scala.util.continuations._

object SimpleCpsExample extends App {


  val v1 = reset {
    shift { k: (Int => Int) =>
      k(7)
    } + 1
  } * 2

  println(s"v1 = $v1")

  val v2 = reset {
    shift { k: (Int => Int) =>
      k(k(k(7)))
    } + 1
  } * 2
  println(s"v2 = $v2")
}