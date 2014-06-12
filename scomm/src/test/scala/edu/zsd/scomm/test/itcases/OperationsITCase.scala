package edu.zsd.scomm.test.itcases

import org.fest.swing.annotation.GUITest
import org.junit.Test
import edu.zsd.scomm.test.FESTTest._
import edu.zsd.scomm.test.useractions.DirectoryListUserActions
import scala.Some
import edu.zsd.scomm.test.FESTTest


@GUITest
class OperationsITCase extends BaseScommITCase {

  @Test
  def testCanceling() {
    operations.copy.openCopyPanel().cancel()
    operations.newFolder.openNewFolderPanel().enterAndCancel("")

    operations.copy.openCopyPanel().cancel()
    operations.newFolder.openNewFolderPanel().enterAndCancel("")
  }

  @Test
  def testViewButton(): Unit = {

    // TODO fix this focus bug so it would works for both directory lists
    for (directoryList: DirectoryListUserActions <- Some(directoriesPane.left)) {
      directoryList.select("folder1")
      FESTTest.mainWindow.requireInfoDialog(1, 0, testDir.toString)

      directoryList.select("a.txt")
      FESTTest.mainWindow.requireInfoDialog(0, 1, testDir.toString)

      directoryList.selectRange("..", "xyz")
      FESTTest.mainWindow.requireInfoDialog(3, 3, testDir.toString)

      directoryList.enterDirectory("zombie")
      FESTTest.mainWindow.requireInfoDialog(0, 0, testDir.resolve("zombie").toString)

      directoryList.selectRange("more", "here")
      FESTTest.mainWindow.requireInfoDialog(2, 1, testDir.resolve("zombie").toString)
    }
  }


}
