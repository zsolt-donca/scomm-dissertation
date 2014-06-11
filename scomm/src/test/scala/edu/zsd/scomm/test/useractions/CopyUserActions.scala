package edu.zsd.scomm.test.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.test.adapters.CopyAdapter

@GUITestBean
class CopyUserActions {

  def copySelection(expectedDestination: String) {
    val copyAdapter = new CopyAdapter
    copyAdapter.requirePanelNotVisible()
    copyAdapter.clickCopy()
    copyAdapter.requirePanelVisible()
    copyAdapter.clickOkButton()
    copyAdapter.waitForPanelToDisappear()
  }
}
