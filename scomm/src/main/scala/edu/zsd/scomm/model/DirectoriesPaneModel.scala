package edu.zsd.scomm.model

import edu.zsd.scomm.Domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.nio.file.Path

@Component
class DirectoriesPaneModel @Autowired()(val left: LeftDirectoryListModel,
                                        val right: RightDirectoryListModel) extends Observing {

  val activeList = Var[DirectoryListModel](left)

  val inactiveList = Var[DirectoryListModel](right)

  val activeCurrentDir: Signal[Path] = Strict {
    activeList().currentDirectory()
  }

  val inactiveCurrentDir: Signal[Path] = Strict {
    inactiveList().currentDirectory()
  }

  val activeSelection: Signal[SelectionInfo] = Strict {
    activeList().selectionInfo()
  }

  val inactiveSelection: Signal[SelectionInfo] = Strict {
    inactiveList().selectionInfo()
  }

  left.active() = true
}
