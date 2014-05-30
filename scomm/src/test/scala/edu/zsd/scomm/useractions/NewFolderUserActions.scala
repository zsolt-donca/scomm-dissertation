package edu.zsd.scomm.useractions

import edu.zsd.testfw.GUITestBean
import edu.zsd.scomm.adapters.NewFolderAdapter

@GUITestBean
class NewFolderUserActions {

  def newFolder(folderName: String) {
    val newFolderAdapter = new NewFolderAdapter
    newFolderAdapter.requirePanelNotVisible()
    newFolderAdapter.clickNewFolder()
    newFolderAdapter.requireFolderName("")
    newFolderAdapter.enterFolderName(folderName)
    newFolderAdapter.clickOkButton()
    newFolderAdapter.requirePanelNotVisible()
  }
}
