package edu.zsd.scomm

import org.junit.Test
import edu.zsd.scomm.FESTTest._
import java.io.File
import org.fest.swing.annotation.GUITest
import org.junit.Assert._
import edu.zsd.scomm.useractions.DirectoriesPaneUserActions

@GUITest
class DirectoriesPaneTest {

  @Test
  def testThatFails(): Unit = {
    println("tralla")
    fail("hoho")
  }

  @Test
  def testSimpleList() {
    val directoryList = new DirectoriesPaneUserActions

    directoryList.left.requireCurrentDir(testDir)
    directoryList.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testEnterDirectory() {
    val directoryList = new DirectoriesPaneUserActions

    directoryList.left.enterDirectory("zombie")
    directoryList.left.requireCurrentDir(testDir + File.separator + "zombie")
    directoryList.left.requireContents(Seq("..", "more", "zombies", "here"))

    directoryList.left.enterParentDirectory()
    directoryList.left.requireCurrentDir(testDir)
    directoryList.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testSelection() {
    val directoryList = new DirectoriesPaneUserActions

    directoryList.left.select("folder1")
    directoryList.left.requireSelection(Seq("folder1"))
    directoryList.left.requireSummary(folders = 1)

    directoryList.left.select("a.txt")
    directoryList.left.requireSelection(Seq("a.txt"))
    directoryList.left.requireSummary(bytes = 3, files = 1)

    directoryList.left.selectRange("..", "xyz")
    directoryList.left.requireSelection(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
    directoryList.left.requireSummary(bytes = 16, files = 3, folders = 4)
  }
}

