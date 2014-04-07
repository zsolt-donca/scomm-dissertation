package edu.zsd.scomm

import org.fest.swing.core.{GenericTypeMatcher, BasicRobot, Robot}
import org.fest.swing.finder.WindowFinder
import java.awt.Frame
import java.io.File
import edu.zsd.scomm.useractions.{MainWindowUserActions, DirectoryListUserActions}

object FESTTest {

  val testDir : String = new File(this.getClass.getClassLoader.getResource("testDir").toURI).getPath
  edu.zsd.scomm.mainWindow.main(Array(testDir))

  val robot: Robot = BasicRobot.robotWithCurrentAwtHierarchy

  val frame = WindowFinder.findFrame(new GenericTypeMatcher[Frame](classOf[Frame], true) {
    def isMatching(frame : Frame) : Boolean = {
      frame.isShowing
    }
  }).using(robot)

  val mainWindow = new MainWindowUserActions

  // test components
  val directoriesPane = new {
    val left = new DirectoryListUserActions("directoriesPane.left")
    val right = new DirectoryListUserActions("directoriesPane.right")
  }
}
