package edu.zsd.scomm.test.adapters

import org.fest.swing.fixture.{JPanelFixture, JLabelFixture, JListFixture}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import edu.zsd.festlogging.{ExecuteInEDT, GUITestBean}
import java.awt.Color
import org.fest.swing.timing.Pause
import java.util.concurrent.TimeUnit

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

  def clickListItem(listItem: String) {
    this.list.item(listItem).click()
    Pause.pause(100, TimeUnit.MILLISECONDS)
  }

  def doubleClickListItem(listItem: String) {
    this.list.item(listItem).doubleClick()
    Pause.pause(100, TimeUnit.MILLISECONDS)
  }

  @ExecuteInEDT
  def requireCurrentDirBackground(color: Color) {
    assertEquals(color, currentDirPanel.component().getBackground)
  }

  override def toString: String = componentName
}
