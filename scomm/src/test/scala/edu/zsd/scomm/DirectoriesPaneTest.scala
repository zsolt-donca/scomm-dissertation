package edu.zsd.scomm

import org.junit.Test
import edu.zsd.scomm.FESTTest._
import java.io.File
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.context.annotation.Configuration
import org.fest.swing.annotation.GUITest

@GUITest
//@RunWith(classOf[SpringJUnit4ClassRunner])
//@ContextConfiguration(classes = Array(classOf[SpringConfig]))
class DirectoriesPaneTest {

  val componentName: String = "directoriesPane"

  @Test
  def testNavigationLeft(): Unit = {
    testNavigation(componentName + ".left")
  }

  @Test
  def testNavigationRight(): Unit = {
    testNavigation(componentName + ".right")
  }

  @Test
  def testSomething() : Unit = {
    println("tralla")
    f()
  }

  def f() = println("hoho")

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

    directoryList.select("folder1")
    directoryList.requireSelection(Seq("folder1"))
    directoryList.requireSummary(folders = 1)

    directoryList.select("a.txt")
    directoryList.requireSelection(Seq("a.txt"))
    directoryList.requireSummary(bytes = 3, files = 1)

    directoryList.selectRange("..", "xyz")
    directoryList.requireSelection(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"))
    directoryList.requireSummary(bytes = 16, files = 3, folders = 4)
  }
}

