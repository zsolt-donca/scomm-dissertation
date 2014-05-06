package edu.zsd.scomm.useractions

import edu.zsd.testfw.GUITestBean
import edu.zsd.scomm.FESTTest._
import edu.zsd.scomm.adapters.MainWindowAdapter
import org.fest.swing.finder.{WindowFinder, JOptionPaneFinder}
import org.junit.Assert._
import org.fest.swing.core.GenericTypeMatcher
import java.awt.Dialog

@GUITestBean
class MainWindowUserActions {

  private[this] val mainWindowAdapter = new MainWindowAdapter

  def requireInfoButtonDisabled() {
    mainWindowAdapter.requireInfoButtonDisabled()
  }

  def requireInfoDialog(directoryCount : Int, fileCount : Int, directory : String) {

    if (mainWindowAdapter.isInfoButtonEnabled) {
      mainWindowAdapter.clickInfoButton()

//      val dialog = WindowFinder.findDialog(new GenericTypeMatcher[Dialog](classOf[Dialog], true){
//        override def isMatching(component: Dialog): Boolean = component.getTitle == "View"
//      }).using(robot)

      val optionPane = JOptionPaneFinder.findOptionPane().using(robot)
      optionPane.requireMessage(s"There are $directoryCount directories and $fileCount files selected in $directory")
      optionPane.okButton().click()
    } else {
      fail("View button should be enabled at this point")
    }

  }
}
