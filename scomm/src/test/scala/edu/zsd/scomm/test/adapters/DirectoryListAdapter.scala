package edu.zsd.scomm.test.adapters

import org.fest.swing.fixture.{JPanelFixture, JLabelFixture, JListFixture}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import edu.zsd.festlogging.{ExecuteInEDT, GUITestBean}
import java.awt.Color

@GUITestBean
case class DirectoryListAdapter(componentName: String) {

  private[this] val list = new JListFixture(robot, componentName + ".listView")
  private[this] val currentDirLabel = new JLabelFixture(robot, componentName + ".currentDirLabel")
  private[this] val currentDirPanel = new JPanelFixture(robot, componentName + ".currentDirPanel")
  private[this] val summary = new JLabelFixture(robot, componentName + ".summaryLabel")

  def currentDir = currentDirLabel.text()

  def requireCurrentDir(currentDir: String): Unit = this.currentDirLabel.requireText(currentDir)

  def requireContents(list: Seq[String]): Unit = assertEquals(list, this.list.contents.toSeq)

  def requireSelection(list: Seq[String]): Unit = assertEquals(list, this.list.selection().toSeq)

  def requireSummary(summary: String): Unit = this.summary.requireText(summary)

  def clickListItem(listItem: String): Unit = this.list.item(listItem).click()

  def doubleClickListItem(listItem: String): Unit = this.list.item(listItem).doubleClick()

  @ExecuteInEDT
  def requireCurrentDirBackground(color: Color): Unit = {
    assertEquals(color, currentDirPanel.component().getBackground)
  }

  def drag(index: Int) {
    list.drag(index)
  }

  def drop() {
    list.drop()
  }

  override def toString: String = componentName
}
