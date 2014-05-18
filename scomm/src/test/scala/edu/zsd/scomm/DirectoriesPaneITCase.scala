package edu.zsd.scomm

import org.junit.{After, Before, Test}
import edu.zsd.scomm.FESTTest._
import org.fest.swing.annotation.GUITest
import org.junit.Assert._
import edu.zsd.testfw.CacioFESTLoggingRunner
import org.junit.runner.RunWith

@GUITest
@RunWith(classOf[CacioFESTLoggingRunner])
class DirectoriesPaneITCase {

  @Before
  def setup() : Unit = {

    val currentDir = directoriesPane.left.getCurrentDir
    if (currentDir != testDir.toString) {
      if (currentDir.startsWith(testDir.toString)) {
        while (directoriesPane.left.getCurrentDir != testDir.toString) {
          directoriesPane.left.enterParentDirectory()
        }
      } else {
        fail("Some test navigated the directories pane outside the testing environment; impossible to fix.")
      }
    }
  }
  
  @After
  def teardown() : Unit = {
    println("teardown")
  }
  
  @Test
  def testThatFails(): Unit = {
    try {
      doSomething()
      fail()
    } catch {
      case _ : Exception =>
    }
    println("tralla")
  }

  def doSomething(): Unit = {
    throw new Exception("hohoho happy xmas")
  }

  @Test
  def testSimpleList() : Unit = {
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testEnterDirectory() : Unit = {
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.enterDirectory("zombie")
    directoriesPane.left.requireCurrentDir(testDir.resolve("zombie").toString)
    directoriesPane.left.requireContents(Seq("..", "more", "zombies", "here"))

    directoriesPane.left.enterParentDirectory()
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.requireContents(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
  }

  @Test
  def testEnterEmptyList() : Unit = {
    directoriesPane.left.requireCurrentDir(testDir.toString)
    directoriesPane.left.enterDirectory("troll")
    directoriesPane.left.requireCurrentDir(testDir.resolve("troll").toString)
    directoriesPane.left.requireContents(Seq(".."))
    directoriesPane.left.enterParentDirectory()
  }

  @Test
  def testSelection() : Unit = {
    directoriesPane.left.select("folder1")
    directoriesPane.left.requireSelection(Seq("folder1"))
    directoriesPane.left.requireSummary(folders = 1)

    directoriesPane.left.select("a.txt")
    directoriesPane.left.requireSelection(Seq("a.txt"))
    directoriesPane.left.requireSummary(bytes = 3, files = 1)

    directoriesPane.left.selectRange("..", "xyz")
    directoriesPane.left.requireSelection(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
    directoriesPane.left.requireSummary(bytes = 16, files = 3, folders = 3)
  }


  @Test
  def testViewButton() : Unit = {

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
}

