package edu.zsd.scomm.test.itcases

import org.fest.swing.annotation.GUITest
import org.junit.{Before, Test}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import java.nio.file.{Files, Path}
import org.junit.Assert._


@GUITest
class NewFolderITCase extends BaseScommITCase {

  @Before
  override def setup() {
    super.setup()
    directoriesPane.ensureLeftIsSelected()
  }

  @Test
  def testNewFolder() {

    val directory: Path = testDir.resolve("troll")
    directoriesPane.left.enterDirectory(directory.getFileName.toString)
    directoriesPane.left.requireCurrentDir(directory)
    directoriesPane.left.requireContents("..")

    operations.newFolder.openNewFolderPanel().enterFolderName("abx")

    val abx: Path = directory.resolve("abx")
    assertTrue("Folder wasn't created?", Files.exists(abx))
    assertTrue("Folder isn't a directory?", Files.isDirectory(abx))

    directoriesPane.left.requireContents("..", "abx")

    Files.delete(abx)
    mainWindow.refresh()
  }

  @Test
  def testCancel() {
    val newFolder = operations.newFolder.openNewFolderPanel()
    newFolder.enterAndCancel("")
    directoriesPane.left.requireContents("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz")
  }

  @Test
  def testFolderNameCleared() {
    operations.newFolder.openNewFolderPanel().enterAndCancel("xx1")
    operations.newFolder.openNewFolderPanel().enterAndCancel("")
  }

}
