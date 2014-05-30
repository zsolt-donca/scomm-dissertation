package edu.zsd.scomm.adapters

import edu.zsd.testfw.{GUITestAction, GUITestBean}
import org.fest.swing.fixture.{JPanelFixture, JButtonFixture, JTextComponentFixture, JLabelFixture}
import edu.zsd.scomm.FESTTest._
import org.fest.swing.core.ComponentMatcher
import java.awt.Component
import edu.zsd.scomm.operations.newfolder.NewFolderPanel
import javax.swing.JPanel
import org.fest.swing.exception.ComponentLookupException


@GUITestBean
class NewFolderAdapter {

  private val newFolderButton = new JButtonFixture(robot, "newFolderButton")

  private lazy val prompt = new JLabelFixture(robot, "newFolder.prompt")

  private lazy val folderName = new JTextComponentFixture(robot, "newFolder.folderName")

  private lazy val okButton = new JButtonFixture(robot, "newFolder.ok")

  private lazy val cancelButton = new JButtonFixture(robot, "newFolder.cancel")

  def requirePanelVisible(): Unit = findPanel.requireVisible()

  def requirePanelNotVisible(): Unit = {
    try {
      val panel = findPanel
      panel.requireNotVisible()
    }
    catch {
      case e: ComponentLookupException =>
    }
  }

  @GUITestAction
  def clickNewFolder(): Unit = newFolderButton.click()

  @GUITestAction
  def clickOkButton(): Unit = okButton.click()

  @GUITestAction
  def clickCancelButton(): Unit = cancelButton.click()

  def requirePrompt(prompt: String): Unit = this.prompt.requireText(prompt)

  def requireFolderName(folderName: String): Unit = this.folderName.requireText(folderName)

  @GUITestAction
  def enterFolderName(folderName: String): Unit = this.folderName.setText(folderName)

  private def findPanel = new JPanelFixture(robot, robot.finder().find(new ComponentMatcher {
    override def matches(c: Component): Boolean = c.getClass == classOf[NewFolderPanel] && c.getName == "newFolderButton"
  }).asInstanceOf[JPanel])
}
