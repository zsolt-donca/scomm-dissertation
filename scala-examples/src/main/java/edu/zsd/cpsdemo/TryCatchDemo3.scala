package edu.zsd.cpsdemo

import scala.util.continuations._

class TryCatchDemo3 {

  def myTry(block: => Int, cat: PartialFunction[String, Int]): Int@cpsParam[Either[Int, String], Either[Int, String]] = {
    val s = shift {
      k: ((=> Int) => Either[Int, String]) =>
        val res: Either[Int, String] = k(block)
        res match {
          case Left(x) => Left(x)
          case Right(e) if cat.isDefinedAt(e) => Left(cat.apply(e))
          case e => e
        }
    }
    s
  }

  def myThrow(e: String): Int@cpsParam[Either[Int, String], Either[Int, String]] = {
    shift {
      k: (Int => Either[Int, String]) =>
        Right(e)
    }
  }

  //  val res: Either[Int, String] = reset {
  //
  //    myTry({
  //      val x = 3
  //      if (x > 3) {
  //        myThrow("Error")
  //      }
  //      x
  //    }, {
  //      case s: String if s.isEmpty => 100
  //    })
  //
  //  }
  //
  //  println(res)
}
