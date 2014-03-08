package edu.zsd.scomm

import org.fest.swing.fixture.JLabelFixture
import org.junit.Test
import edu.zsd.scomm.FESTTest._

class DirectoriesPaneTest {

  @Test
  def test01() : Unit = {
    val leftCurrentDir = new JLabelFixture(robot, "directoriesPane.leftDirectoryList.currentDirLabel")
    leftCurrentDir.requireText("C:\\")
    
    val rightCurrentDir = new JLabelFixture(robot, "directoriesPane.rightDirectoryList.currentDirLabel")
    rightCurrentDir.requireText("D:\\")
  }
}

