package edu.zsd.cpsdemo

//
//import scala.util.continuations._
//
//object MyTryCatchDemo extends App {
//
//  class CatchBlock[R](val pf: PartialFunction[Any, R])
//
//  def myTry[R](tryBlock: => R, catchBlock: CatchBlock[R]): Either[R, Any] = {
//
//    shift {
//      f: ()
//    }
//
//    Left(tryBlock)
//  }
//
//  def myThrow[R](e: Any): Either[R, Any] = {
//    shift {
//      f: (R => Either[R, Any]) =>
//        Right(e)
//    }
//  }
//
//  def myCatch[R](pf: PartialFunction[Any, R]) = {
//    new CatchBlock[R](pf)
//  }
//
//  def parseInt(s: String): Int => {
//    val num: Either[Int, Any] = reset {
//    val x = myTry ( {
//    1
//  }, myCatch {
//    case e: String =>
//    println (s"Exception: $e")
//    - 1
//  })
//    x
//  }
//    num
//  }
//
//
//  println(s"num: $num")
//}
