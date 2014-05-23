package edu.zsd.scomm.operations

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.{SelectionInfo, MainWindowModel}
import edu.zsd.scomm.Domain._
import java.nio.file.Path

@Component
class CopyModel @Autowired()(val model: MainWindowModel) extends Observing {

  val sourceDir: Signal[Path] = Strict {
    model.directoriesPaneModel.activeList().currentDir()
  }

  val selection: Signal[SelectionInfo] = Strict {
    model.directoriesPaneModel.activeList().selectionInfo()
  }

  val destination: Signal[Path] = Strict {
    model.directoriesPaneModel.inactiveList().currentDir()
  }

}
