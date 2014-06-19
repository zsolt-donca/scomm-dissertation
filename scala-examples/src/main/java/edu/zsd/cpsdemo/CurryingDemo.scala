package edu.zsd.cpsdemo

object CurryingDemo extends App {

  //  def sum(a: Int): (Int => Int) = b => a + b
  def sum(a: Int)(b: Int) = a + b

  println(sum(1)(2))

  val plusOne: (Int => Int) = sum(1)
  println(plusOne(2))

  val totalLength = List("Joe", "Jack").foldLeft(0)((len, name) => len + name.length)
  println(totalLength)

  class Point(val x: Double, val y: Double)

  class Circle(val center: Point, val radius: Double)

  def isInside(c: Circle): (Point => Boolean) = {
    import java.lang.Math.pow
    val radiusSquare = pow(c.radius, 2)

    (p: Point) => {
      val distSquare = pow(p.x - c.center.x, 2) + pow(p.y - c.center.y, 2)
      radiusSquare >= distSquare
    }
  }

  val myCircle = new Circle(new Point(1, 2), 2)
  val points = Seq(new Point(2, 3), new Point(4, 2), new Point(1, 1))
  val isInsideMyCircle = isInside(myCircle)
  val inside = points.map(p => isInsideMyCircle(p))
  println(inside) // List(true, false, true)

  {
    // partial application
    def sum3(a: Int, b: Int, c: Int) = a + b + c

    val sum2: ((Int, Int) => Int) = sum3(1, _: Int, _: Int)
    println(sum2(2, 3)) // prints 6
  }
  {
    // curried version
    def sum3(a: Int)(b: Int)(c: Int) = a + b + c

    val sum2: (Int => Int => Int) = sum3(1)
    println(sum2(2)(3)) // prints 6
  }
}
