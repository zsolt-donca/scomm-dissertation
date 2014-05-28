package edu.zsd.scomm.useractions

import edu.zsd.testfw.GUITestBean
import edu.zsd.scomm.adapters.NewFolderAdapter

@GUITestBean
class NewFolderUserActions {

  private val newFolderAdapter = new NewFolderAdapter

  def newFolder(folderName: String) {
    newFolderAdapter.clickNewFolder()
    newFolderAdapter.requireFolderName("")
    newFolderAdapter.enterFolderName(folderName)
    newFolderAdapter.clickOkButton()
  }

  // TODO find a way for the below checks to work
  private def requireNotVisible(action: => Unit) {
    newFolderAdapter.requirePanelNotVisible()
    action
    newFolderAdapter.requirePanelNotVisible()
  }

}
