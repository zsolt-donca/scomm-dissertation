package edu.zsd.scomm.model

import edu.zsd.scomm.Domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

@Component
class DirectoriesPaneModel @Autowired()(val left: LeftDirectoryListModel,
                                        val right: RightDirectoryListModel) extends Observing {

  val activeList = Var[DirectoryListModel](left)
  val inactiveList = Var[DirectoryListModel](right)

  left.active() = true
}
