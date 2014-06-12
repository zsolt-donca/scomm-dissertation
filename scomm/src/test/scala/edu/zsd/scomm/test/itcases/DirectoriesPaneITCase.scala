package edu.zsd.scomm.test.itcases

import org.junit.Test
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.fest.swing.annotation.GUITest
import edu.zsd.scomm.test.useractions.DirectoryListUserActions
import org.fest.swing.timing.Pause
import java.util.concurrent.TimeUnit

@GUITest
class DirectoriesPaneITCase extends BaseScommITCase {

  @Test
  def testSimpleList(): Unit = {
    for (directoryList: DirectoryListUserActions <- directoriesPane.directoryLists) {
      directoryList.requireCurrentDir(testDir.toString)
      directoryList.requireContents("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz")
    }
  }

  @Test
  def testEnterDirectory(): Unit = {
    for (directoryList: DirectoryListUserActions <- directoriesPane.directoryLists) {
      directoryList.requireCurrentDir(testDir.toString)
      directoryList.enterDirectory("zombie")
      directoryList.requireCurrentDir(testDir.resolve("zombie").toString)
      directoryList.requireContents("..", "more", "zombies", "here")

      directoryList.enterParentDirectory()
      directoryList.requireCurrentDir(testDir.toString)
      directoryList.requireContents("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz")
    }
  }

  @Test
  def testEnterEmptyList(): Unit = {
    for (directoryList: DirectoryListUserActions <- directoriesPane.directoryLists) {
      directoryList.requireCurrentDir(testDir.toString)
      directoryList.enterDirectory("troll")
      directoryList.requireCurrentDir(testDir.resolve("troll").toString)
      directoryList.requireContents("..")
      directoryList.enterParentDirectory()
    }
  }

  @Test
  def testSelection(): Unit = {
    for (directoryList: DirectoryListUserActions <- directoriesPane.directoryLists) {
      directoryList.select("folder1")
      directoryList.requireSelection("folder1")
      directoryList.requireSummary(folders = 1)

      directoryList.select("a.txt")
      directoryList.requireSelection("a.txt")
      directoryList.requireSummary(bytes = 3, files = 1)

      directoryList.selectRange("..", "xyz")
      directoryList.requireSelection("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz")
      directoryList.requireSummary(bytes = 16, files = 3, folders = 3)
    }
  }


  @Test
  def testTabSwitchingActiveness(): Unit = {
    directoriesPane.left.select("folder1")
    directoriesPane.left.requireActive()
    directoriesPane.right.requireInactive()

    directoriesPane.right.select("folder1")
    directoriesPane.right.requireActive()
    directoriesPane.left.requireInactive()
  }

  @Test
  def testTabSwitchingSelectionCleared(): Unit = {
    directoriesPane.left.select("folder1")
    directoriesPane.left.requireSelection("folder1")
    directoriesPane.right.requireSelection()

    directoriesPane.right.select("folder1")
    directoriesPane.right.requireSelection("folder1")
    directoriesPane.left.requireSelection()
  }

  @Test
  def testNavigationAutoSelectsDestination(): Unit = {
    for (directoryList: DirectoryListUserActions <- directoriesPane.directoryLists) {
      directoryList.select("folder1")

      directoryList.enterDirectory("zombie")
      directoryList.requireSelection("..")

      directoryList.enterDirectory("zombies")
      directoryList.requireSelection("..")

      directoryList.enterParentDirectory()
      directoryList.requireSelection("zombies")

      Pause.pause(200, TimeUnit.MILLISECONDS)
      directoryList.enterParentDirectory()
      directoryList.requireSelection("zombie")
    }
  }
}

