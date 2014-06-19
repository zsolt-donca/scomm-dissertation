package edu.zsd.cpsdemo

//import scala.util.continuations.{cpsParam, cps}
//import scala.util.continuations._
//
//object MonadicDemo {
//
//  type Monadic[+U, C[_]] = {
//    def flatMap[V](f: U => C[V]): C[V]
//  }
//
//  class Reflective[+A, C[_]](xs: Monadic[A, C]) {
//    def reflect[B](): A@cpsParam[C[B], C[B]] = {
//      shift { k: (A => C[B]) =>
//        xs.flatMap(k)
//      }
//    }
//  }
//
//  implicit def reflective[A](xs: Iterable[A]) = new Reflective[A, Iterable](xs)
//
//  val prod = reset {
//    val left = List("x", "y", "z")
//    val right = List(4, 5, 6)
//    val prod = List((left.reflect[Any](), right.reflect[Any]()))
//  }
//  println(prod)
//
//  // result: cartesian product
//
//}
