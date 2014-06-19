package edu.zsd.cpsdemo


//object DynamicAnswerTypeCpsDemo extends App {
//
//  def cps_block[A, B, C](f: => A): A@cpsParam[B, C] = {
//    f
//  }
//
//  val v: String = reset {
//
//    val items: List[Any] = shift {
//      k: (List[Any] => List[Long]) =>
//        val items: List[Any] = List(1, 2L, "3", '4')
//        val longs: List[Long] = k(items)
//        longs.mkString(", ")
//    }
//
//    var longs: List[Long] = List.empty
//    val i: Iterator[Any] = items.iterator;
//    /*cps_block[Unit, List[Long], String]*/
//    {
//      while (i.hasNext) {
//        val n = i.next()
//
//        if (n.isInstanceOf[Int]) {
//          val int = n.asInstanceOf[Int]
//          longs :::= shift {
//            k: (Long => List[Long]) =>
//              -1L :: k(int.toLong)
//          }
//        }
//        n match {
//          case long: Long => longs ::= long
//          case str: String => longs ::= str.toInt
//          case char: Char => longs ::= (char.toInt - '0'.toInt)
//        }
//      }
//    }
//    longs
//
//  }
//
//  println(v)
//
//}
