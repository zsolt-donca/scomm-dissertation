package edu.zsd.scomm

import scala.util.continuations._

object TwelveDaysOfChristmas extends App {

  val daysAndGifts = Seq(
    ("First", "a Patridge in a Pear Tree"),
    ("Second", "Two Turtle Doves"),
    ("Third", "Three French Hens")
  )
  val days = daysAndGifts.map(_._1)
  val gifts = daysAndGifts.map(_._2)

  reset {
    val dayIndex = shift { verse: (Int => Unit) =>
      for (dayIndex <- 0 to days.length - 1) {
        verse(dayIndex)
      }
    }

    val day = days(dayIndex)
    println(s"On the $day day of Christmas my true love sent to me")

    val giftIndex = shift { k: (Int => Unit) =>
      for (giftIndex <- dayIndex to 0 by -1) {
        k(giftIndex)
      }
      println("")
    }

    val gift = gifts(giftIndex)
    if (dayIndex == 0) {
      println(s"$gift.")
    } else if (giftIndex > 0) {
      println(s"$gift,")
    } else {
      println(s"and $gift.")
    }
  }

}
