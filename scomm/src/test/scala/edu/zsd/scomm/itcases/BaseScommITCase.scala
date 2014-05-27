package edu.zsd.scomm.itcases

import org.junit.Before
import edu.zsd.scomm.FESTTest._
import org.junit.Assert._
import org.junit.runner.RunWith
import edu.zsd.testfw.CacioFESTLoggingRunner
import edu.zsd.scomm.useractions.DirectoryListUserActions

@RunWith(classOf[CacioFESTLoggingRunner])
abstract class BaseScommITCase {

  @Before
  def setup(): Unit = {
    navigateToTestRoot(directoriesPane.left)
    navigateToTestRoot(directoriesPane.right)
  }

  def navigateToTestRoot(directoryList: DirectoryListUserActions) {
    val currentDir = directoryList.currentDir
    if (currentDir != testDir.toString) {
      if (currentDir.startsWith(testDir.toString)) {
        while (directoriesPane.left.currentDir != testDir.toString) {
          directoriesPane.left.enterParentDirectory()
        }
      } else {
        fail("Some test navigated the directories pane outside the testing environment; impossible to fix.")
      }
    }
  }
}
