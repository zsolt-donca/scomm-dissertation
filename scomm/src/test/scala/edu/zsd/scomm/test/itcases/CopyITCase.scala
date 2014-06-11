package edu.zsd.scomm.test.itcases

import org.fest.swing.annotation.GUITest
import org.junit.Test
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import java.nio.file.{Path, Files}


@GUITest
class CopyITCase extends BaseScommITCase {

  @Test
  def copySelection() {

    directoriesPane.left.enterDirectory("troll")
    directoriesPane.right.enterDirectory("zombie")
    directoriesPane.right.selectRange("zombies", "here")

    val trollPath = testDir.resolve("troll")
    operations.copy.copySelection(trollPath.toString)

    directoriesPane.left.requireContents("..", "zombies", "here")

    val zombiesPath: Path = trollPath.resolve("zombies")
    assertTrue(Files.exists(zombiesPath))
    assertTrue(Files.isDirectory(zombiesPath))

    val herePath: Path = trollPath.resolve("here")
    assertTrue(Files.exists(herePath))
    assertTrue(Files.isRegularFile(herePath))

    Files.delete(zombiesPath)
    Files.delete(herePath)
    mainWindow.refresh()
  }

}
