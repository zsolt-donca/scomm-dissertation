package edu.zsd.scomm.model

import edu.zsd.scomm.domain._

class DirectoriesPaneModel(val left : DirectoryListModel,
                           val right : DirectoryListModel) extends Observing {

  val activeList = Strict[Option[DirectoryListModel]] {
    if (left.active()) {
      Some(left)
    } else if (right.active()) {
      Some(right)
    } else {
      None
    }
  }

  left.active() = true
}
