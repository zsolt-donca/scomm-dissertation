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

  def tryParse(str: String): Int@cpsParam[Int, Either[Int, String]] = {
    if (str.matches("\\s*")) {
      myThrow("Is empty!")
    } else if (str.matches("\\d+")) {
      just(str.toInt)
    } else {
      myThrow(s"Is not number: $str")
    }
  }

  def tolerantParse(str: String): Either[Int, String] = {
    reset {
      myCatch {
        case s: String if s.contains("empty") => 0
      }

      tryParse(str)
    }
  }

  println(tolerantParse("1"))
  println(tolerantParse("x"))
  println(tolerantParse(""))
  println(tolerantParse("1234"))
  println(tolerantParse("          "))
  println(tolerantParse("001234"))

}
