package edu.zsd.scomm

import org.fest.swing.core.{GenericTypeMatcher, BasicRobot, Robot}
import org.fest.swing.finder.WindowFinder
import java.awt.Frame
import java.io.File

object FESTTest {

  val testDir : String = new File(this.getClass.getClassLoader.getResource("testDir").toURI).getPath
  mainWindow.main(Array(testDir))

  val robot: Robot = BasicRobot.robotWithCurrentAwtHierarchy

  val frame = WindowFinder.findFrame(new GenericTypeMatcher[Frame](classOf[Frame], true) {
    def isMatching(frame : Frame) : Boolean = {
      "Scala Commander".equals(frame.getTitle) && frame.isShowing
    }
  }).using(robot)
}
