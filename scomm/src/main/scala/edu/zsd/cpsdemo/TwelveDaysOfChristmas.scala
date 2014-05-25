package edu.zsd.cpsdemo

import scala.util.continuations._

object TwelveDaysOfChristmas extends App {

  val daysAndGifts = Seq(
    ("First", "a Patridge in a Pear Tree"),
    ("Second", "Two Turtle Doves"),
    ("Third", "Three French Hens")
  )
  val days = daysAndGifts.map(_._1)
  val gifts = daysAndGifts.map(_._2)

  val carol: List[String] = reset {
    val dayIndex: Int = shift {
      verse: (Int => List[String]) =>
        (0 to days.length - 1).foldRight(List.empty[String])((day, list) => "" :: verse(day) ::: list)
    }

    val dayLine = s"On the ${days(dayIndex)} day of Christmas my true love sent to me"

    val giftIndex: Int = shift {
      line: (Int => String) =>
        dayLine :: (0 to dayIndex).foldLeft(List.empty[String])((list, gift) => line(gift) :: list)
    }

    val gift = gifts(giftIndex)
    val line = if (dayIndex == 0) {
      s"$gift."
    } else if (giftIndex > 0) {
      s"$gift,"
    } else {
      s"and $gift."
    }
    line
  }

  carol.foreach(println(_))
}
