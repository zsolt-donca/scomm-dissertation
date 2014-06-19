package edu.zsd.cpsdemo

import scala.util.continuations._

object Explaining2CpsDemo extends App {

  def charToHexString(char: Char): String = {
    reset {
      val s: Char = shift {
        k: (Char => Int) =>
          val num: Int = k(char)
          "0x%02x".format(num) // Int to String (hex code)
      }
      s.toInt // converts Char to Int
    }
  }

  println(charToHexString('A'))

  {
    val reset: String = {
      val shift: ((Char => Int) => String) = {
        k: (Char => Int) =>
          val num: Int = k('A')
          "0x%02x".format(num) // Int to String (hex code)
      }
      val rest: (Char => Int) = {
        s: Char =>
          s.toInt // converts Char to Int
      }
      shift(rest)
    }
    println(reset) // prints 0x41
  }
}
