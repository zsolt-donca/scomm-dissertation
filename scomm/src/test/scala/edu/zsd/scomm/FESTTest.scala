package edu.zsd.scomm

import org.fest.swing.core.{ComponentLookupScope, GenericTypeMatcher, BasicRobot, Robot}
import org.fest.swing.finder.WindowFinder
import java.awt.Frame
import java.io.File
import edu.zsd.scomm.useractions.{NewFolderUserActions, MainWindowUserActions, DirectoryListUserActions}
import java.nio.file.{DirectoryStream, Files, Path}
import scala.collection.JavaConverters._

object FESTTest {

  val testDir: Path = new File(this.getClass.getClassLoader.getResource("testDir").toURI).toPath
  deleteEmptyDirectoryPlaceholders(testDir)
  edu.zsd.scomm.Main.main(Array(testDir.toString))

  val robot: Robot = BasicRobot.robotWithCurrentAwtHierarchy
  robot.settings().componentLookupScope(ComponentLookupScope.ALL)

  val frame = WindowFinder.findFrame(new GenericTypeMatcher[Frame](classOf[Frame], true) {
    def isMatching(frame: Frame): Boolean = {
      frame.isShowing
    }
  }).using(robot)

  val mainWindow = new MainWindowUserActions

  // test components
  val directoriesPane = new {
    val left = new DirectoryListUserActions("directoriesPane.left")
    val right = new DirectoryListUserActions("directoriesPane.right")
    val directoryLists = Seq(left, right)
  }

  val operations = new {
    val newFolder = new NewFolderUserActions
  }

  private def deleteEmptyDirectoryPlaceholders(dir: Path) {
    val emptyPlaceholder: Path = dir.resolve("empty")
    if (Files.exists(emptyPlaceholder)) {
      Files.delete(emptyPlaceholder)
    }
    val directoryStream: DirectoryStream[Path] = Files.newDirectoryStream(dir)
    try {
      val files = directoryStream.asScala
      for (file <- files if Files.isDirectory(file)) deleteEmptyDirectoryPlaceholders(file)
    } finally {
      directoryStream.close()
    }
  }


}
