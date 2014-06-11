package edu.zsd.scomm.test.useractions

import edu.zsd.scomm.test.FESTTest
import FESTTest._
import java.awt.event.KeyEvent
import edu.zsd.festlogging.{GUITestAction, GUITestBean}
import edu.zsd.scomm.test.adapters.DirectoryListAdapter
import java.awt.Color
import java.nio.file.Path
import org.fest.swing.timing.Pause

@GUITestBean
case class DirectoryListUserActions(directoryListAdapter: DirectoryListAdapter) {

  def currentDir = directoryListAdapter.currentDir

  def requireCurrentDir(currentDir: String): Unit = directoryListAdapter.requireCurrentDir(currentDir)

  def requireCurrentDir(currentDir: Path): Unit = requireCurrentDir(currentDir.toString)

  def requireContents(list: String*) = directoryListAdapter.requireContents(list)

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
}
