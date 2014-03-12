package edu.zsd.scomm

import org.fest.swing.fixture.JLabelFixture
import org.junit.Test
import edu.zsd.scomm.FESTTest._
import java.io.File
import java.awt.event.KeyEvent

class DirectoriesPaneTest {

  val componentName: String = "directoriesPane"

  @Test
  def testNavigation(): Unit = {
    testNavigation(componentName + ".left")
    testNavigation(componentName + ".right")
  }

  def testNavigation(componentName: String) {
    val directoryList = new DirectoryListUserActions(componentName)

    directoryList.requireCurrentDir(testDir)
    directoryList.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))

    directoryList.enterDirectory("zombie")

    directoryList.requireCurrentDir(testDir + File.separator + "zombie")
    directoryList.requireContents(Seq("..", "more", "zombies", "here"))

    directoryList.enterParentDirectory()
    directoryList.requireCurrentDir(testDir)
    directoryList.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))

    directoryList.select("zombie")
    directoryList.requireSelection(Seq("zombie"))

    directoryList.selectRange("..", "xyz")
    directoryList.requireSelection(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
    directoryList.requireSummary(bytes = 16, files = 3, folders = 4)

    directoryList.select("folder1")
    directoryList.requireSelection(Seq("folder1"))
    directoryList.requireSummary(bytes = 0, files = 0, folders = 1)
  }
}

