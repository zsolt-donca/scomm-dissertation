package edu.zsd.scomm.useractions

import edu.zsd.festlogging.GUITestBean
import edu.zsd.scomm.adapters.DirectoriesPaneAdapter

@GUITestBean
class DirectoriesPaneUserActions {

  protected[useractions] val directoriesPaneAdapter = new DirectoriesPaneAdapter

  val left = new DirectoryListUserActions(directoriesPaneAdapter.left)
  val right = new DirectoryListUserActions(directoriesPaneAdapter.right)
  val directoryLists = Seq(left, right)

}
