package edu.zsd.scomm.test.adapters

import edu.zsd.festlogging.{ExecuteInEDT, GUITestAction, GUITestBean}
import org.fest.swing.fixture.JButtonFixture
import edu.zsd.scomm.test.FESTTest
import FESTTest._
import org.junit.Assert._
import javax.swing.SwingUtilities.isEventDispatchThread

@GUITestBean
class MainWindowAdapter {

  private val infoButton = new JButtonFixture(robot, "infoButton")

  private val refreshButton = new JButtonFixture(robot, "refreshButton")

  @ExecuteInEDT
  def isInfoButtonEnabled: Boolean = {
    assertTrue(isEventDispatchThread)
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
