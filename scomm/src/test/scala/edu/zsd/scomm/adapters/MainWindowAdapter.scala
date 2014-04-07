package edu.zsd.scomm.adapters

import edu.zsd.testfw.{GUITestAction, GUITestBean}
import org.fest.swing.fixture.JButtonFixture
import edu.zsd.scomm.FESTTest._
import org.fest.swing.edt.GuiQuery
import org.fest.swing.edt.GuiActionRunner._

@GUITestBean
class MainWindowAdapter {

  private[this] val infoButton = new JButtonFixture(robot, "infoButton")

  def isInfoButtonEnabled : Boolean = {
    execute(new GuiQuery[Boolean] {
      override def executeInEDT(): Boolean = infoButton.component.isEnabled
    })
  }

  def requireInfoButtonDisabled() {
    infoButton.requireDisabled()
  }

  @GUITestAction
  def clickInfoButton() = {
    infoButton.click()
  }
}
