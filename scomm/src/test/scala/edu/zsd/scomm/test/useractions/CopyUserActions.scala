package edu.zsd.scomm.test.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.test.adapters.CopyAdapter
import edu.zsd.scomm.test.FESTTest._

@GUITestBean
class CopyUserActions {

  def openCopyPanel() = {
    val copyAdapter = new CopyAdapter
    copyAdapter.requirePanelNotVisible()
    mainWindow.openCopyPanel()
    copyAdapter.requirePanelVisible()
    new {
      def ok() {
        copyAdapter.clickOkButton()
        copyAdapter.waitForPanelToDisappear()
      }

      def cancel() {
        copyAdapter.clickCancelButton()
        copyAdapter.waitForPanelToDisappear()
      }

      def enterDestination(folderName: String) {
        copyAdapter.enterDestination(folderName)
      }

      def requireDestination(directory: String) {
        copyAdapter.requireDestination(directory)
      }

      def requirePrompt(prompt: String) {
        copyAdapter.requirePrompt(prompt)
      }

      def requireSingleFilePrompt(file: String) {
        requirePrompt(s"Copy the file '$file' to:")
      }

      def requireSingleFolderPrompt(folder: String) {
        requirePrompt(s"Copy the folder '$folder' and its contents to:")
      }

      def requireFilesAndFoldersPrompt(files: Int, folders: Int) {
        requirePrompt(s"Copy the selected $files file(s) and $folders folder(s) and all their contents to:")
      }

      def requireFilesPrompt(files: Int) {
        requirePrompt(s"Copy the selected $files files to:")
      }

      def requireFoldersPrompt(folders: Int) {
        requirePrompt(s"Copy the selected $folders folders and all their contents to:")
      }

      def requireNothingToCopyPrompt() {
        requirePrompt("Nothing to copy!")
      }
    }
  }
}
