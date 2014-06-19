package edu.zsd.cpsdemo

import scala.annotation.tailrec
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

  val sum = (0 to 100).sum
  println(sum)

  def factorial(n: Int): Int = {
    @tailrec def factorialAcc(acc: Int, n: Int): Int = {
      if (n <= 1) acc
      else factorialAcc(n * acc, n - 1)
    }
    factorialAcc(1, n)
  }

  println(s"factorial(7) = ${factorial(7)}")


}
