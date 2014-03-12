package edu.zsd.scomm

import org.junit.Assert._
import edu.zsd.scomm.FESTTest._
import java.awt.event.KeyEvent

class DirectoryListUserActions(componentName: String) {

  private [this] val directoryListAdapter = new DirectoryListAdapter(componentName)

  def requireCurrentDir(currentDir : String) : Unit = directoryListAdapter.requireCurrentDir(currentDir)

  def requireContents(list : Seq[String]) : Unit = directoryListAdapter.requireContents(list)

  def requireSelection(list : Seq[String]) : Unit = directoryListAdapter.requireSelection(list)

  def requireSummary(bytes : Int, files : Int, folders : Int) : Unit = {
    directoryListAdapter.requireSummary(s"$bytes bytes, $files file(s), $folders folder(s)")
  }

  def enterDirectory(directory : String) : Unit = directoryListAdapter.doubleClickListItem(directory)

  def enterParentDirectory() : Unit = enterDirectory("..")

  def select(item : String) : Unit = directoryListAdapter.clickListItem(item)

  def selectRange(from : String, to : String) : Unit = {
    directoryListAdapter.clickListItem(from)
    robot.pressKey(KeyEvent.VK_SHIFT)
    directoryListAdapter.clickListItem(to)
    robot.releaseKey(KeyEvent.VK_SHIFT)
  }
}
