package edu.zsd.scomm.adapters

import edu.zsd.festlogging.{ExecuteInEDT, GUITestAction, GUITestBean}
import org.fest.swing.fixture.JButtonFixture
import edu.zsd.scomm.FESTTest._
import org.junit.Assert._
import javax.swing.SwingUtilities.isEventDispatchThread

@GUITestBean
class MainWindowAdapter {

  private[this] val infoButton = new JButtonFixture(robot, "infoButton")

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
}
