package edu.zsd.scomm.model

import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

@Component
class DirectoriesPaneModel @Autowired() (val left : LeftDirectoryListModel,
                                         val right : RightDirectoryListModel) extends Observing {

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
