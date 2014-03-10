package edu.zsd.scomm

import org.fest.swing.fixture.{JListFixture, JLabelFixture}
import org.junit.Test
import org.junit.Assert._
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
    val list = new JListFixture(robot, componentName + ".listView")
    val currentDir = new JLabelFixture(robot, componentName + ".currentDirLabel")
    val summary = new JLabelFixture(robot, componentName + ".summaryLabel")

    currentDir.requireText(testDir)
    assertEquals(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"), list.contents.toSeq)

    list.item("zombie").doubleClick()

    currentDir.requireText(testDir + File.separator + "zombie")
    assertEquals(Seq("..", "more", "zombies", "here"), list.contents.toSeq)

    list.item("..").doubleClick()
    currentDir.requireText(testDir)
    assertEquals(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"), list.contents.toSeq)

    list.item("zombie").click()
    assertEquals(Seq("zombie"), list.selection.toSeq)

    list.item("..").click()
    robot.pressKey(KeyEvent.VK_SHIFT)
    list.item("xyz").click()
    robot.releaseKey(KeyEvent.VK_SHIFT)
    assertEquals(Seq("..", "folder1", "troll", "zombie", "a.txt", "b", "xyz"), list.selection.toSeq)
    summary.requireText("16 bytes, 3 file(s), 4 folder(s)")

    list.item("folder1").click()
    assertEquals(Seq("folder1"), list.selection.toSeq)
    summary.requireText("0 bytes, 0 file(s), 1 folder(s)")
  }
}

