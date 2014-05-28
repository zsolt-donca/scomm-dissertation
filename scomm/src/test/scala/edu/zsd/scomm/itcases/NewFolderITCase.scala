package edu.zsd.scomm.itcases

import org.fest.swing.annotation.GUITest
import org.junit.Test
import edu.zsd.scomm.FESTTest._
import java.nio.file.{Files, Path}
import org.junit.Assert._


@GUITest
class NewFolderITCase extends BaseScommITCase {

  @Test
  def testNewFolder() {

    val directory: Path = testDir.resolve("troll")
    directoriesPane.left.enterDirectory("troll")
    directoriesPane.left.requireCurrentDir(directory)
    directoriesPane.left.requireContents("..")

    operations.newFolder.newFolder("abx")
    val testNewFolder: Path = directory.resolve("abx")
    assertTrue("Folder wasn't created?", Files.exists(testNewFolder))
    assertTrue("Folder isn't a directory?", Files.isDirectory(testNewFolder))

    directoriesPane.left.requireContents("..", "abx")

    Files.delete(testNewFolder)
  }

}
