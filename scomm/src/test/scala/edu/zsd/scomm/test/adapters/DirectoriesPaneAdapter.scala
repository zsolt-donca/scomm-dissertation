package edu.zsd.scomm.test.adapters

import edu.zsd.festlogging.GUITestBean

@GUITestBean
class DirectoriesPaneAdapter {

  val left = new DirectoryListAdapter("directoriesPane.left")
  val right = new DirectoryListAdapter("directoriesPane.right")

}
