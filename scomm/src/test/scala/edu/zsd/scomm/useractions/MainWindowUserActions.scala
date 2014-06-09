package edu.zsd.scomm.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.FESTTest._
import edu.zsd.scomm.adapters.MainWindowAdapter
import org.fest.swing.finder.JOptionPaneFinder
import org.junit.Assert._

@GUITestBean
class MainWindowUserActions {

  private[this] val mainWindowAdapter = new MainWindowAdapter

  def requireInfoButtonDisabled() {
    mainWindowAdapter.requireInfoButtonDisabled()
  }

  def requireInfoDialog(directoryCount: Int, fileCount: Int, directory: String) {

    if (mainWindowAdapter.isInfoButtonEnabled) {
      mainWindowAdapter.clickInfoButton()

      val optionPane = JOptionPaneFinder.findOptionPane().using(robot)
      try {
        optionPane.requireMessage(s"There are $directoryCount directories and $fileCount files selected in $directory")
      }
      finally {
        optionPane.okButton().click()
      }
    } else {
      fail("View button should be enabled at this point")
    }
  }

  def refresh() {
    mainWindowAdapter.clickRefreshButton()
  }
}
