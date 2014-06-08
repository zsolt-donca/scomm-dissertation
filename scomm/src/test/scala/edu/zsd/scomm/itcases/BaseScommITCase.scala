package edu.zsd.scomm.itcases

import org.junit.Before
import edu.zsd.scomm.FESTTest._
import org.junit.Assert._
import org.junit.runner.RunWith
import edu.zsd.festlogging.CacioFESTLoggingRunner
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
        var cycles = 0
        while (directoryList.currentDir != testDir.toString) {
          directoryList.enterParentDirectory()
          cycles += 1
          if (cycles > 20) {
            fail("Got into an infinite cycle? Current dir: " + directoryList.currentDir)
          }
        }
      } else {
        fail("Some test navigated the directories pane outside the testing environment; impossible to fix.")
      }
    }
  }
}
