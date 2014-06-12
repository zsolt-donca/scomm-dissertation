package edu.zsd.scomm.test.adapters

import org.fest.swing.fixture.{JPanelFixture, JLabelFixture, JListFixture}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import edu.zsd.festlogging.{ExecuteInEDT, GUITestBean}
import java.awt.Color

@GUITestBean
case class DirectoryListAdapter(componentName: String) extends BaseAdapter {

  private[this] val list = new JListFixture(robot, componentName + ".listView")
  private[this] val currentDirLabel = new JLabelFixture(robot, componentName + ".currentDirLabel")
  private[this] val currentDirPanel = new JPanelFixture(robot, componentName + ".currentDirPanel")
  private[this] val summary = new JLabelFixture(robot, componentName + ".summaryLabel")

  def currentDir = currentDirLabel.text()

  def requireCurrentDir(currentDir: String): Unit = this.currentDirLabel.requireText(currentDir)

  def requireContents(list: Seq[String]): Unit = assertEquals(list, this.list.contents.toSeq)

  def requireSelection(list: Seq[String]): Unit = assertEquals(list, this.list.selection().toSeq)

  def requireSummary(summary: String): Unit = this.summary.requireText(summary)

  def clearSelection() {
    this.list.clearSelection()
    smallPause()
  }

  def clickListItem(listItem: String) {
    this.list.item(listItem).click()
    smallPause()
  }

  def doubleClickListItem(listItem: String) {
    this.list.item(listItem).doubleClick()
    smallPause()
  }

  @ExecuteInEDT
  def currentDirBackground() = {
    currentDirPanel.component().getBackground
  }

  def requireCurrentDirBackground(color: Color) {
    assertEquals(color, currentDirBackground())
  }

  override def toString: String = componentName
}
