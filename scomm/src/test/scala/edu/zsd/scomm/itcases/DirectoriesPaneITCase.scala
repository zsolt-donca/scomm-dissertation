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
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testEnterDirectory(): Unit = {
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.enterDirectory("zombie")
    directoriesPane.left.requireCurrentDir(testDir.resolve("zombie").toString)
    directoriesPane.left.requireContents(Seq("..", "more", "zombies", "here"))

    directoriesPane.left.enterParentDirectory()
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testEnterEmptyList(): Unit = {
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.enterDirectory("troll")
    directoriesPane.left.requireCurrentDir(testDir.resolve("troll").toString)
    directoriesPane.left.requireContents(Seq(".."))
    directoriesPane.left.enterParentDirectory()
  }

  @Test
  def testSelection(): Unit = {
    directoriesPane.left.select("folder1")
    directoriesPane.left.requireSelection("folder1")
    directoriesPane.left.requireSummary(folders = 1)

    directoriesPane.left.select("a.txt")
    directoriesPane.left.requireSelection("a.txt")
    directoriesPane.left.requireSummary(bytes = 3, files = 1)

    directoriesPane.left.selectRange("..", "xyz")
    directoriesPane.left.requireSelection("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz")
    directoriesPane.left.requireSummary(bytes = 16, files = 3, folders = 3)
  }


  @Test
  def testViewButton(): Unit = {

    directoriesPane.left.select("folder1")
    FESTTest.mainWindow.requireInfoDialog(1, 0, testDir.toString)

    directoriesPane.left.select("a.txt")
    FESTTest.mainWindow.requireInfoDialog(0, 1, testDir.toString)

    directoriesPane.left.selectRange("..", "xyz")
    FESTTest.mainWindow.requireInfoDialog(3, 3, testDir.toString)

    directoriesPane.left.enterDirectory("zombie")
    FESTTest.mainWindow.requireInfoDialog(0, 0, testDir.resolve("zombie").toString)

    directoriesPane.left.selectRange("more", "here")
    FESTTest.mainWindow.requireInfoDialog(2, 1, testDir.resolve("zombie").toString)
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
    for (directoryList: DirectoryListUserActions <- Seq(directoriesPane.left, directoriesPane.right)) {
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

