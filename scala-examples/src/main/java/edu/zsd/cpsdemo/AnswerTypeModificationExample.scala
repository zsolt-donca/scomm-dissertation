package edu.zsd.cpsdemo

import scala.util.continuations._

object AnswerTypeModificationExample extends App {

  class Poly(val nums: Seq[Int]) {
    override def toString: String = nums.zipWithIndex.reverse.map {
      case (c: Int, 0) => s"$c"
      case (c: Int, 1) => s"$c*x"
      case (c: Int, p: Int) => s"$c*x^$p"
    }.mkString(" + ") + " = 0"
  }

  def toStrings(strings: Seq[String]): Seq[String]@cpsParam[Seq[Int], Poly] = {
    shift {
      f: (Seq[String] => Seq[Int]) =>
        new Poly(f(strings))
    }
  }

  def toInts(strings: Seq[String]): Int@cpsParam[Int, Seq[Int]] = {
    val num: String = shift {
      f: (String => Int) =>
        val ints: Seq[Int] = strings.filter(_.matches("\\d+")).map(f)
        ints
    }
    num.toInt
  }

  def toPoly(strings: Seq[String]): Poly = reset {

    val str = toStrings(strings)
    val i: Int = toInts(str)
    i + 1
  }


  println(toPoly(Seq("1", "20", "?", "300", "y?", "4000")))
}
