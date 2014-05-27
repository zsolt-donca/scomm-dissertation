package edu.zsd.scomm.useractions

import edu.zsd.scomm.FESTTest._
import java.awt.event.KeyEvent
import edu.zsd.testfw.{GUITestAction, GUITestBean}
import edu.zsd.scomm.adapters.DirectoryListAdapter
import java.awt.Color

@GUITestBean
case class DirectoryListUserActions(componentName: String) {

  private[this] val directoryListAdapter = new DirectoryListAdapter(componentName)

  def currentDir = directoryListAdapter.currentDir

  def requireCurrentDir(currentDir: String) = directoryListAdapter.requireCurrentDir(currentDir)

  def requireContents(list: Seq[String]) = directoryListAdapter.requireContents(list)

  def requireSelection(list: String*) = directoryListAdapter.requireSelection(list)

  def requireSummary(bytes: Int = 0, files: Int = 0, folders: Int = 0) {
    directoryListAdapter.requireSummary(s"$bytes bytes, $files file(s), $folders folder(s)")
  }

  @GUITestAction
  def enterDirectory(directory: String) = directoryListAdapter.doubleClickListItem(directory)

  @GUITestAction
  def enterParentDirectory() = enterDirectory("..")

  @GUITestAction
  def select(item: String) = directoryListAdapter.clickListItem(item)

  @GUITestAction
  def selectRange(from: String, to: String): Unit = {
    directoryListAdapter.clickListItem(from)
    robot.pressKey(KeyEvent.VK_SHIFT)
    directoryListAdapter.clickListItem(to)
    robot.releaseKey(KeyEvent.VK_SHIFT)
  }

  def requireActive() = directoryListAdapter.requireCurrentDirBackground(Color.BLUE)

  def requireInactive() = directoryListAdapter.requireCurrentDirBackground(Color.LIGHT_GRAY)


  override def toString: String = componentName
}
