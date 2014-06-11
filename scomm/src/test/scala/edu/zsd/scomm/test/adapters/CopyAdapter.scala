package edu.zsd.scomm.test.adapters

import edu.zsd.festlogging.{GUITestAction, GUITestBean}
import org.fest.swing.fixture.{JPanelFixture, JButtonFixture}
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.fest.swing.exception.ComponentLookupException
import org.fest.swing.timing.{Condition, Pause}

@GUITestBean
class CopyAdapter extends CopyMovePanelAdapter {

  private val copyButton = new JButtonFixture(robot, "copyButton")

  def requirePanelVisible(): Unit = findPanel.requireVisible()

  def requirePanelNotVisible(): Unit = {
    try {
      findPanel.requireNotVisible()
    } catch {
      case e: ComponentLookupException => // not finding the component is okay
    }
  }

  def waitForPanelToDisappear(): Unit = {
    Pause.pause(new Condition("wait for copy panel to disappear") {
      override def test(): Boolean = try {
        findPanel
        false
      } catch {
        case e: ComponentLookupException => true
      }
    })
  }

  @GUITestAction
  def clickCopy(): Unit = copyButton.click()

  @GUITestAction
  def clickOkButton(): Unit = components.okButton.click()

  @GUITestAction
  def clickCancelButton(): Unit = components.cancelButton.click()

  def requirePrompt(prompt: String): Unit = components.prompt.requireText(prompt)

  def requireDestination(folderName: String): Unit = components.destination.requireText(folderName)

  @GUITestAction
  def enterDestination(folderName: String): Unit = components.destination.setText(folderName)

  private def findPanel = new JPanelFixture(robot, "copyPanel")
}
