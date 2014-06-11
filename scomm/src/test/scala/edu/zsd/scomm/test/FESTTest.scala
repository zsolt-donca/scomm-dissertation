package edu.zsd.scomm.test

import java.nio.file.{DirectoryStream, Files, Path}
import java.io.File
import org.fest.swing.core.{GenericTypeMatcher, ComponentLookupScope, BasicRobot, Robot}
import org.fest.swing.finder.WindowFinder
import java.awt.Frame
import edu.zsd.scomm.test.useractions.{CopyUserActions, NewFolderUserActions, DirectoriesPaneUserActions, MainWindowUserActions}
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
  val directoriesPane = new DirectoriesPaneUserActions

  val operations = new {
    val newFolder = new NewFolderUserActions
    val copy = new CopyUserActions
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
