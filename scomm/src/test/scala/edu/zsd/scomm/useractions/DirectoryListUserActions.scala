package edu.zsd.scomm.useractions

import edu.zsd.scomm.FESTTest._
import java.awt.event.KeyEvent
import edu.zsd.testfw.{GUITestAction, GUITestBean}
import edu.zsd.scomm.adapters.DirectoryListAdapter

@GUITestBean
case class DirectoryListUserActions(componentName: String) {

  private [this] val directoryListAdapter = new DirectoryListAdapter(componentName)

  def requireCurrentDir(currentDir : String) : Unit = {
    directoryListAdapter.requireCurrentDir(currentDir)
  }

  def requireContents(list : Seq[String]) : Unit = {
    directoryListAdapter.requireContents(list)
  }

  def requireSelection(list : Seq[String]) : Unit = {
    directoryListAdapter.requireSelection(list)
  }

  def requireSummary(bytes : Int = 0, files : Int = 0, folders : Int = 0) : Unit = {
    directoryListAdapter.requireSummary(s"$bytes bytes, $files file(s), $folders folder(s)")
  }

  @GUITestAction
  def enterDirectory(directory : String) : Unit = {
    directoryListAdapter.doubleClickListItem(directory)
  }

  @GUITestAction
  def enterParentDirectory() : Unit = {
    enterDirectory("..")
  }

  @GUITestAction
  def select(item : String) : Unit = {
    directoryListAdapter.clickListItem(item)
  }

  @GUITestAction
  def selectRange(from : String, to : String) : Unit = {
    directoryListAdapter.clickListItem(from)
    robot.pressKey(KeyEvent.VK_SHIFT)
    directoryListAdapter.clickListItem(to)
    robot.releaseKey(KeyEvent.VK_SHIFT)
  }

  override def toString: String = componentName
}
