package edu.zsd.scomm.test.itcases

import org.fest.swing.annotation.GUITest
import org.junit.{Before, Test}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import java.nio.file.{Path, Files}
import org.apache.commons.io.FileUtils


@GUITest
class CopyITCase extends BaseScommITCase {


  @Before
  override def setup() {
    super.setup()
    directoriesPane.ensureLeftIsSelected()
  }

  @Test
  def testCopySelection() {

    directoriesPane.left.enterDirectory("troll")
    directoriesPane.right.enterDirectory("zombie")
    directoriesPane.right.selectRange("zombies", "here")

    val trollPath = testDir.resolve("troll")
    val copy = operations.copy.openCopyPanel()
    copy.requireDestination(testDir.resolve("troll").toString)
    copy.ok()

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

  @Test
  def testCopySingleFile() {
    directoriesPane.left.enterDirectory("troll")
    directoriesPane.right.select("a.txt")

    val copy = operations.copy.openCopyPanel()
    copy.requireOneFilePrompt("a.txt")
    copy.requireDestination(testDir.resolve("troll").toString)
    copy.ok()

    val dest: Path = testDir.resolve("troll").resolve("a.txt")
    assertTrue(Files.exists(dest) && Files.isRegularFile(dest))
    Files.delete(dest)
    mainWindow.refresh()
  }

  @Test
  def testCopySingleFolder() {
    directoriesPane.left.enterDirectory("troll")
    directoriesPane.right.select("zombie")

    val copy = operations.copy.openCopyPanel()
    copy.requireOneFolderPrompt("zombie")
    copy.requireDestination(testDir.resolve("troll").toString)
    copy.ok()

    val dest: Path = testDir.resolve("troll").resolve("zombie")
    assertTrue(Files.exists(dest) && Files.isDirectory(dest))
    assertTrue(Files.isDirectory(dest.resolve("more")))
    assertTrue(Files.isDirectory(dest.resolve("zombies")))
    assertTrue(Files.isRegularFile(dest.resolve("here")))
    FileUtils.deleteDirectory(dest.toFile)
    mainWindow.refresh()
  }
}
