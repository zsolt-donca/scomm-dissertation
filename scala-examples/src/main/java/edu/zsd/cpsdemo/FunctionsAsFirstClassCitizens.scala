package edu.zsd.cpsdemo

object FunctionsAsFirstClassCitizens extends App {

  val plusOne: (Int => Int) = x => x + 1
  println(plusOne(2))
  // prints 3

  val p1: Function1[Int, Int] = plusOne

  val fibo: PartialFunction[Int, String] = {
    case 1 => "one"
    case 2 => "two"
    case 3 => "three"
    case 5 => "five"
    case 8 => "eight"
  }

  println(fibo.isDefinedAt(2) + " " + fibo(2)) // prints: true two
  println(fibo.isDefinedAt(4)) // prints "false"

  // prints "one two three five eight"
  (0 to 10).collect(fibo).foreach(x => print(x + " "))
}
