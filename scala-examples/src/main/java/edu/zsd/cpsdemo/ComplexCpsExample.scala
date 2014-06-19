package edu.zsd.cpsdemo

import scala.util.continuations._

object ComplexCpsExample extends App with StrictLogging {

  def twice(x: Int): Int@cpsParam[Int, Int] = {
    println(s"shift begins, x = $x")
    val r = shift {
      k: (Int => Int) =>
        val c: Int = k(x)
        val d = k(c)
        d
    } + 1
    println(s"shift ends, x = $x, r = $r")
    r
  }

  val val1 = reset {
    println(s"reset begins")

    var v = 0
    //    var i = 0
    //    while(i < 2) {
    //      v = twice(v)
    //      i += 1
    //    }
    v = twice(v)
    v = twice(v)

    println(s"reset ends with $v")
    v
  }
  println(s"val1 = $val1")
}
