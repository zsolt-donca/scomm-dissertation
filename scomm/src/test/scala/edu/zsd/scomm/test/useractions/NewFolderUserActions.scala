package edu.zsd.scomm.test.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.test.adapters.NewFolderAdapter
import edu.zsd.scomm.test.FESTTest.mainWindow

@GUITestBean
class NewFolderUserActions {

  def newFolder() = {
    val newFolderAdapter = new NewFolderAdapter
    newFolderAdapter.requirePanelNotVisible()
    mainWindow.openNewFolderPanel()
    newFolderAdapter.requireFolderName("")
    new {
      def enterFolderName(folderName: String) {
        newFolderAdapter.enterFolderName(folderName)
        newFolderAdapter.clickOkButton()
        newFolderAdapter.requirePanelNotVisible()
      }

      def enterAndCancel(folderName: String) {
        if (folderName.nonEmpty) {
          newFolderAdapter.enterFolderName(folderName)
        }
        newFolderAdapter.clickCancelButton()
        newFolderAdapter.requirePanelNotVisible()
      }
    }
  }
}
