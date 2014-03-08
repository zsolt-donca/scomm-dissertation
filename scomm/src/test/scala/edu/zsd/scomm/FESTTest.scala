package edu.zsd.scomm

import org.junit.{Before, Test}
import org.fest.swing.core.{GenericTypeMatcher, Robot, BasicRobot}
import java.awt.Frame
import org.fest.swing.finder.WindowFinder
import org.fest.swing.fixture.JLabelFixture

class FESTTest {

  @Before
  def setup() = {
    mainWindow.main(Array.empty)
  }

  @Test
  def test01() : Unit = {
    val robot: Robot = BasicRobot.robotWithCurrentAwtHierarchy
    val frame = WindowFinder.findFrame(new GenericTypeMatcher[Frame](classOf[Frame], true) {
      def isMatching(frame : Frame) : Boolean = {
        "Scala Commander".equals(frame.getTitle) && frame.isShowing
      }
    }).using(robot)

    val leftCurrentDir = new JLabelFixture(robot, "leftDirectoryList.currentDirLabel")
    leftCurrentDir.requireText("C:\\")

    val rightCurrentDir = new JLabelFixture(robot, "rightDirectoryList.currentDirLabel")
    rightCurrentDir.requireText("D:\\")

  }
}
