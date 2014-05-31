package edu.zsd.cpsdemo

import scala.util.continuations._


object MyTryCatchDemo4 extends App {

  def myCatch(cat: PartialFunction[String, Int]): Unit@cpsParam[Either[Int, String], Either[Int, String]] = {
    shift {
      k: (Unit => Either[Int, String]) =>
        val res: Either[Int, String] = k()
        res match {
          case Left(x) => Left(x)
          case Right(e) if cat.isDefinedAt(e) => Left(cat.apply(e))
          case e => e
        }
    }
  }

  def myThrow(e: String): Int@cpsParam[Int, Either[Int, String]] = {
    shift {
      k: (Int => Int) =>
        Right(e)
    }
  }

  def just(i: Int): Int@cpsParam[Int, Either[Int, String]] = {
    shift {
      k: (Int => Int) =>
        Left(i)
    }
  }

  def parse(str: String): Either[Int, String] = {
    reset {
      myCatch {
        case s: String if s.contains("empty") => 0
      }

      if (str.isEmpty) {
        myThrow("Is empty!")
      } else if (str.matches("\\d+")) {
        just(str.toInt)
      } else {
        myThrow(s"Is not number: $str")
      }
    }
  }

  println(parse("1"))
  println(parse("x"))
  println(parse(""))
  println(parse("1234"))
  println(parse("001234"))

}
