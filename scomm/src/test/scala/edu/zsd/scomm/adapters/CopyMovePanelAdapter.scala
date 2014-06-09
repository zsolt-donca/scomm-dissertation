package edu.zsd.scomm.adapters

import edu.zsd.festlogging.GUITestBean
import org.fest.swing.fixture.{JTextComponentFixture, JLabelFixture, JButtonFixture}
import edu.zsd.scomm.FESTTest._

@GUITestBean
class CopyMovePanelAdapter {

  protected lazy val components = new {
    val prompt = new JLabelFixture(robot, "copyMovePanel.prompt")
    val destination = new JTextComponentFixture(robot, "copyMovePanel.destination")
    val okButton = new JButtonFixture(robot, "copyMovePanel.ok")
    val cancelButton = new JButtonFixture(robot, "copyMovePanel.cancel")
  }


}
