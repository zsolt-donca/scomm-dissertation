package edu.zsd.cpsdemo

//
//import scala.util.continuations._
//
//object MyTryCatch2Demo extends App {
//
//  def myTry[R <: AnyRef](body: => R): R @cpsParam[R, Either[R, Exception]] = {
//    shift {
//      f: ((=> R) => R) =>
//        Left[R, Exception](f)
//    }
//    body
//  }
//
//  def myThrow[R <: AnyRef](e: Exception): R @cpsParam[R, Either[R, Exception]] = {
//    shift {
//      f: (R => R) =>
//        Right[R, Exception](e)
//    }
//    null.asInstanceOf[R]
//  }
//
//}
