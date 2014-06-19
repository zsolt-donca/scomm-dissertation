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

    // the continuation below this line calculated a single verse for the day identified by `dayIndex`

    val dayLine = s"On the ${days(dayIndex)} day of Christmas my true love sent to me"

    val giftIndex: Int = shift {
      line: (Int => String) =>
        dayLine :: (0 to dayIndex).foldLeft(List.empty[String])((list, gift) => line(gift) :: list)
    }

    // the continuation below this line calculates a single line of a verse, the line identified by `giftIndex`

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

  // print each line to the console
  carol.foreach(println(_))

  /*
  On the First day of Christmas my true love sent to me
  a Patridge in a Pear Tree.

  On the Second day of Christmas my true love sent to me
  Two Turtle Doves,
  and a Patridge in a Pear Tree.

  On the Third day of Christmas my true love sent to me
  Three French Hens,
  Two Turtle Doves,
  and a Patridge in a Pear Tree.
   */
}
