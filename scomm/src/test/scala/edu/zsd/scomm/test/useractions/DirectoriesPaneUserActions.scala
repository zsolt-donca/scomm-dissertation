package edu.zsd.scomm.test.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.test.adapters.DirectoriesPaneAdapter
import edu.zsd.scomm.test.FESTTest._
import org.fest.swing.timing.Pause
import java.util.concurrent.TimeUnit

@GUITestBean
class DirectoriesPaneUserActions {

  protected[useractions] val directoriesPaneAdapter = new DirectoriesPaneAdapter

  val left = new DirectoryListUserActions(directoriesPaneAdapter.left)
  val right = new DirectoryListUserActions(directoriesPaneAdapter.right)
  val directoryLists = Seq(left, right)

  def ensureSelectLeft() {
    if (left.isInactive) {
      directoriesPane.right.select("..")
      directoriesPane.left.select("..")
      Pause.pause(100, TimeUnit.MILLISECONDS)
      directoriesPane.left.requireActive()
    }
  }
}
