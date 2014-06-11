package edu.zsd.scomm.test.adapters

import edu.zsd.festlogging.{ExecuteInEDT, GUITestAction, GUITestBean}
import org.fest.swing.fixture.JButtonFixture
import edu.zsd.scomm.test.FESTTest
import FESTTest._

@GUITestBean
class MainWindowAdapter {

  private val infoButton = new JButtonFixture(robot, "infoButton")

  private val refreshButton = new JButtonFixture(robot, "refreshButton")

  @ExecuteInEDT
  def isInfoButtonEnabled: Boolean = {
    infoButton.component.isEnabled
  }

  def requireInfoButtonDisabled() {
    infoButton.requireDisabled()
  }

  @GUITestAction
  def clickInfoButton() = {
    infoButton.click()
  }

  @GUITestAction
  def clickRefreshButton() = {
    refreshButton.click()
  }
}
