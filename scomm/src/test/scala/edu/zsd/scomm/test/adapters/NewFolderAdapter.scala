package edu.zsd.scomm.test.adapters

import edu.zsd.festlogging.{GUITestAction, GUITestBean}
import org.fest.swing.fixture.{JPanelFixture, JButtonFixture, JTextComponentFixture, JLabelFixture}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.fest.swing.exception.ComponentLookupException


@GUITestBean
class NewFolderAdapter {

  private lazy val components = new {
    val prompt = new JLabelFixture(robot, "newFolder.prompt")
    val folderName = new JTextComponentFixture(robot, "newFolder.folderName")
    val okButton = new JButtonFixture(robot, "newFolder.ok")
    val cancelButton = new JButtonFixture(robot, "newFolder.cancel")
  }

  def requirePanelVisible(): Unit = findPanel.requireVisible()

  def requirePanelNotVisible(): Unit = {
    try {
      findPanel.requireNotVisible()
    } catch {
      case e: ComponentLookupException => // not finding the component is okay
    }
  }

  @GUITestAction
  def clickOkButton(): Unit = components.okButton.click()

  @GUITestAction
  def clickCancelButton(): Unit = components.cancelButton.click()

  def requirePrompt(prompt: String): Unit = components.prompt.requireText(prompt)

  def requireFolderName(folderName: String): Unit = components.folderName.requireText(folderName)

  @GUITestAction
  def enterFolderName(folderName: String): Unit = components.folderName.setText(folderName)

  private def findPanel = new JPanelFixture(robot, "newFolder")
}
