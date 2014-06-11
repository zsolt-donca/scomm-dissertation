package edu.zsd.scomm.test.itcases

import org.junit.Test
import org.junit.Assert._
import org.fest.swing.annotation.GUITest

@GUITest
class GeneralITCase extends BaseScommITCase {

  @Test
  def testThatFails(): Unit = {
    try {
      doSomething()
      fail()
    } catch {
      case _: Exception =>
    }
  }

  def doSomething(): Unit = {
    throw new Exception("hohoho happy xmas")
  }

}
