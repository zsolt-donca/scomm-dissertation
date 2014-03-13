package edu.zsd.scomm

import org.fest.swing.fixture.{JLabelFixture, JListFixture}
import edu.zsd.scomm.FESTTest._
import org.junit.Assert._
import edu.zsd.testfw.GUITestBean

@GUITestBean
class DirectoryListAdapter(componentName: String) {

  private[this] val list = new JListFixture(robot, componentName + ".listView")
  private[this] val currentDir = new JLabelFixture(robot, componentName + ".currentDirLabel")
  private[this] val summary = new JLabelFixture(robot, componentName + ".summaryLabel")

  def requireCurrentDir(currentDir : String) : Unit = this.currentDir.requireText(currentDir)

  def requireContents(list : Seq[String]) : Unit = assertEquals(list, this.list.contents.toSeq)
  
  def requireSelection(list : Seq[String]) : Unit = assertEquals(list, this.list.selection().toSeq)

  def requireSummary(summary : String) : Unit = this.summary.requireText(summary)

  def clickListItem(listItem : String) : Unit = this.list.item(listItem).click()

  def doubleClickListItem(listItem : String) : Unit = this.list.item(listItem).doubleClick()
}
