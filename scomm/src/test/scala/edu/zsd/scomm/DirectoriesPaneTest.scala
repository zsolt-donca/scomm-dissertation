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
    val directoryList: DirectoryListAdapter = new DirectoryListAdapter(componentName)

    directoryList.requireCurrentDir(testDir)
    directoryList.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))

    directoryList.doubleClickListItem("zombie")

    directoryList.requireCurrentDir(testDir + File.separator + "zombie")
    directoryList.requireContents(Seq("..", "more", "zombies", "here"))

    directoryList.doubleClickListItem("..")
    directoryList.requireCurrentDir(testDir)
    directoryList.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))

    directoryList.clickListItem("zombie")
    directoryList.requireSelection(Seq("zombie"))

    directoryList.clickListItem("..")
    robot.pressKey(KeyEvent.VK_SHIFT)
    directoryList.clickListItem("xyz")
    robot.releaseKey(KeyEvent.VK_SHIFT)
    directoryList.requireSelection(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
    directoryList.requireSummary("16 bytes, 3 file(s), 4 folder(s)")

    directoryList.clickListItem("folder1")
    directoryList.requireSelection(Seq("folder1"))
    directoryList.requireSummary("0 bytes, 0 file(s), 1 folder(s)")
  }
}

