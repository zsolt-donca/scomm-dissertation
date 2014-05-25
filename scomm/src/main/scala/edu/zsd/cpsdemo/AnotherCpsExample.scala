package edu.zsd.cpsdemo

import scala.util.continuations._

object AnotherCpsExample extends App {

  val result = reset {
    println("entering first shift")
    val firstShift = shift { k: (Int => Int) =>
      val res = k(0)
      println(s"exiting first shift, res = $res")
      res
    } + 1

    println(s"firstShift = $firstShift; entering second shift")
    val secondShift = shift { k: (Int => Int) =>
      val res: Int = k(firstShift)
      println(s"exiting second shift, res = $res")
      res
    } + 1
    println(s"secondShift = $secondShift; returning the reset")

    secondShift
  }

  println(s"result = $result")

  /* Produces the below result:

  entering first shift
  firstShift = 1; entering second shift
  secondShift = 2; returning the reset
  exiting second shift, res = 2
  exiting first shift, res = 2
  result = 2
  */

}
