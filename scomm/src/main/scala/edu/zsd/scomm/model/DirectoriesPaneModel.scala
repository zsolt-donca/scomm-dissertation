package edu.zsd.scomm.model

import edu.zsd.scomm.Domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.nio.file.Path

@Component
class DirectoriesPaneModel @Autowired()(val left: LeftDirectoryListModel,
                                        val right: RightDirectoryListModel) extends Observing {

  val activeListModel = Var[DirectoryListModel](left)

  val inactiveListModel = Var[DirectoryListModel](right)

  val activeCurrentDir: Signal[Path] = Strict {
    activeListModel().currentDirectory()
  }

  val inactiveCurrentDir: Signal[Path] = Strict {
    inactiveListModel().currentDirectory()
  }

  val activeSelection: Signal[SelectionInfo] = Strict {
    activeListModel().selectionInfo()
  }

  val inactiveSelection: Signal[SelectionInfo] = Strict {
    inactiveListModel().selectionInfo()
  }

  left.active() = true
}
