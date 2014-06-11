package edu.zsd.scomm.test.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.test.adapters.CopyAdapter
import edu.zsd.scomm.test.FESTTest._

@GUITestBean
class CopyUserActions {

  def copySelection(expectedDestination: String) {
    val copyAdapter = new CopyAdapter
    copyAdapter.requirePanelNotVisible()
    mainWindow.openCopyPanel()
    copyAdapter.requirePanelVisible()
    copyAdapter.clickOkButton()
    copyAdapter.waitForPanelToDisappear()
  }
}
