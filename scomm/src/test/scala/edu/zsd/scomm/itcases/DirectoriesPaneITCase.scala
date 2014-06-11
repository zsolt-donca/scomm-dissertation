package edu.zsd.scomm.itcases

import org.junit.Test
import edu.zsd.scomm.FESTTest._
import org.fest.swing.annotation.GUITest
import edu.zsd.scomm.FESTTest
import edu.zsd.scomm.useractions.DirectoryListUserActions

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
  def testViewButton(): Unit = {

    // TODO fix this focus bug so it would works for both directory lists
    for (directoryList: DirectoryListUserActions <- Some(directoriesPane.left)) {
      directoryList.select("folder1")
      FESTTest.mainWindow.requireInfoDialog(1, 0, testDir.toString)

      directoryList.select("a.txt")
      FESTTest.mainWindow.requireInfoDialog(0, 1, testDir.toString)

      directoryList.selectRange("..", "xyz")
      FESTTest.mainWindow.requireInfoDialog(3, 3, testDir.toString)

      directoryList.enterDirectory("zombie")
      FESTTest.mainWindow.requireInfoDialog(0, 0, testDir.resolve("zombie").toString)

      directoryList.selectRange("more", "here")
      FESTTest.mainWindow.requireInfoDialog(2, 1, testDir.resolve("zombie").toString)
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

      directoryList.enterParentDirectory()
      directoryList.requireSelection("zombie")
    }
  }
}

