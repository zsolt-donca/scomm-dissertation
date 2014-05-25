package edu.zsd.cpsdemo

import scala.util.continuations._

object ExplainingCpsDemo extends App {

  val simple: Int = reset {
    val a = 40
    val b = 2
    a + b
  }
  println(simple)
  // 42

  val result = reset {
    val s = shift {
      k: (Int => Int) =>
        k(1) + 7
    }
    s + 4
  }
  println(result) // prints 12

  {
    val reset = {
      val shift: ((Int => Int) => Int) = {
        k: (Int => Int) =>
          k(1) + 7
      }
      val rest: (Int => Int) = {
        s: Int =>
          s + 4
      }
      shift(rest)
    }
    println(reset) // prints 12
  }

  val names = reset {
    val names = Seq("Joe", "Bill", "Jack")
    for (name <- names) {
      println(name)
    }
    names
  }
}
