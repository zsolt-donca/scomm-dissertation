package edu.zsd.scomm.test.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.test.adapters.NewFolderAdapter

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
