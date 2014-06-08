package edu.zsd.festlogging

import java.io.File

object FESTLogging {

  val baseDir = new File("reports").getAbsoluteFile
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
