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
    val directoriesPane = new DirectoriesPaneUserActions

    directoriesPane.left.requireCurrentDir(testDir)
    directoriesPane.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testEnterDirectory() {
    val directoriesPane = new DirectoriesPaneUserActions

    directoriesPane.left.enterDirectory("zombie")
    directoriesPane.left.requireCurrentDir(testDir + File.separator + "zombie")
    directoriesPane.left.requireContents(Seq("..", "more", "zombies", "here"))

    directoriesPane.left.enterParentDirectory()
    directoriesPane.left.requireCurrentDir(testDir)
    directoriesPane.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testSelection() {
    val directoriesPane = new DirectoriesPaneUserActions

    directoriesPane.left.select("folder1")
    directoriesPane.left.requireSelection(Seq("folder1"))
    directoriesPane.left.requireSummary(folders = 1)

    directoriesPane.left.select("a.txt")
    directoriesPane.left.requireSelection(Seq("a.txt"))
    directoriesPane.left.requireSummary(bytes = 3, files = 1)

    directoriesPane.left.selectRange("..", "xyz")
    directoriesPane.left.requireSelection(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
    directoriesPane.left.requireSummary(bytes = 16, files = 3, folders = 4)
  }
}

