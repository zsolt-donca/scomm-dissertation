package edu.zsd.testfw

import java.io.File

object FESTLogging {

  val baseDir = new File("./reports")
  baseDir.mkdirs()
  
  val xmlDir = new File(baseDir, "xml")
  xmlDir.mkdir()
  
  val screenshotsDir = new File(baseDir, "shots")
  screenshotsDir.mkdir()

  private var testCount = 0

  def beginTest() {
    testCount += 1
  }

  def currentTestIndex = testCount

}
