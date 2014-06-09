package edu.zsd.scomm.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.adapters.CopyAdapter
import edu.zsd.scomm.FESTTest._
import java.awt.event.KeyEvent

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

  def copyWithDragAndDrop(source: Int) {

    robot.pressKey(KeyEvent.VK_CONTROL)
    try {
      directoriesPane.directoriesPaneAdapter.right.drag(source)
      directoriesPane.directoriesPaneAdapter.left.drop()
    } finally {
      robot.releaseKey(KeyEvent.VK_CONTROL)
    }
  }
}
