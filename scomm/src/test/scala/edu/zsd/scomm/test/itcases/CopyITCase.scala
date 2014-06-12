package edu.zsd.scomm.test.itcases

import org.fest.swing.annotation.GUITest
import org.junit.{Before, Test}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import java.nio.file.{Path, Files}
import org.apache.commons.io.FileUtils
import java.awt.event.KeyEvent


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
    copy.requireFilesAndFoldersPrompt(1, 1)
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
    copy.requireSingleFilePrompt("a.txt")
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
    copy.requireSingleFolderPrompt("zombie")
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

  @Test
  def testChangeSelection() {
    val copy = operations.copy.openCopyPanel()

    directoriesPane.left.deselectAll()
    copy.requireNothingToCopyPrompt()

    directoriesPane.left.select("..")
    copy.requireNothingToCopyPrompt()

    directoriesPane.left.select("folder1")
    copy.requireSingleFolderPrompt("folder1")

    directoriesPane.left.select("troll")
    copy.requireSingleFolderPrompt("troll")

    directoriesPane.left.select("zombie")
    copy.requireSingleFolderPrompt("zombie")

    directoriesPane.left.select("a.txt")
    copy.requireSingleFilePrompt("a.txt")

    directoriesPane.left.select("b")
    copy.requireSingleFilePrompt("b")

    directoriesPane.left.select("xyz")
    copy.requireSingleFilePrompt("xyz")

    directoriesPane.left.select("folder1")
    copy.requireSingleFolderPrompt("folder1")
    robot.pressKey(KeyEvent.VK_CONTROL)
    directoriesPane.left.select("troll")
    copy.requireFoldersPrompt(2)
    directoriesPane.left.select("a.txt")
    copy.requireFilesAndFoldersPrompt(1, 2)
    robot.releaseKey(KeyEvent.VK_CONTROL)
    copy.cancel()
  }
}
