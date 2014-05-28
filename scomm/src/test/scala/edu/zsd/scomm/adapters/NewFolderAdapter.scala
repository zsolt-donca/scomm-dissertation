package edu.zsd.scomm.adapters

import edu.zsd.testfw.{GUITestAction, GUITestBean}
import org.fest.swing.fixture.{JPanelFixture, JButtonFixture, JTextComponentFixture, JLabelFixture}
import edu.zsd.scomm.FESTTest._


@GUITestBean
class NewFolderAdapter {

  private val newFolderButton = new JButtonFixture(robot, "newFolderButton")

  // TODO realize to have the below variables live for a test and not for just a single method
  private def panel = new JPanelFixture(robot, "newFolder")

  private def prompt = new JLabelFixture(robot, "newFolder.prompt")

  private def folderName = new JTextComponentFixture(robot, "newFolder.folderName")

  private def okButton = new JButtonFixture(robot, "newFolder.ok")

  private def cancelButton = new JButtonFixture(robot, "newFolder.cancel")

  def requirePanelVisible(): Unit = panel.requireVisible()

  def requirePanelNotVisible(): Unit = panel.requireNotVisible()

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

}
