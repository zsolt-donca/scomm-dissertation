package edu.zsd.scomm.useractions

import edu.zsd.testfw.GUITestBean

@GUITestBean
class DirectoriesPaneUserActions {

  val left = new DirectoryListUserActions("directoriesPane.left")

  val right = new DirectoryListUserActions("directoriesPane.right")
}
