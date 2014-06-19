package edu.zsd.cpsdemo

import scala.util.continuations._


object MyTryCatchDemo4 extends App {

  def myCatch[R, E](cat: PartialFunction[E, R]): Unit@cpsParam[Either[R, E], Either[R, E]] = {
    shift {
      k: (Unit => Either[R, E]) =>
        val res: Either[R, E] = k()
        res match {
          case Left(x) => Left(x)
          case Right(e) if cat.isDefinedAt(e) => Left(cat.apply(e))
          case e => e
        }
    }
  }

  def myThrow[R, E](e: E): R@cpsParam[R, Either[R, E]] = {
    shift {
      k: (R => R) =>
        Right(e)
    }
  }

  def just[R, E](i: R): R@cpsParam[R, Either[R, E]] = {
    shift {
      k: (R => R) =>
        Left(i)
    }
  }

  def parse(str: String): Int@cpsParam[Int, Either[Int, String]] = {
    if (str.matches("\\s*")) {
      myThrow("Is empty!")
    } else if (str.matches("\\d+")) {
      val int: Int = str.toInt
      just(int)
    } else {
      myThrow(s"Is not number: $str")
    }
  }

  def tolerantParse(str: String): Either[Int, String] = {
    reset {
      myCatch[Int, String] {
        case s: String if s.contains("empty") => 0
      }

      parse(str)
    }
  }

  println(tolerantParse("1"))
  println(tolerantParse("x"))
  println(tolerantParse(""))
  println(tolerantParse("1234"))
  println(tolerantParse("          "))
  println(tolerantParse("001234"))

}
