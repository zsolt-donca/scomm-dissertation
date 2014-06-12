package edu.zsd.scomm.test.itcases

import org.fest.swing.annotation.GUITest
import org.junit.Test
import edu.zsd.scomm.test.FESTTest.operations


@GUITest
class OperationsITCase extends BaseScommITCase {

  @Test
  def testCanceling() {
    operations.copy.openCopyPanel().cancel()
    operations.newFolder.openNewFolderPanel().enterAndCancel("")

    operations.copy.openCopyPanel().cancel()
    operations.newFolder.openNewFolderPanel().enterAndCancel("")
  }

}
