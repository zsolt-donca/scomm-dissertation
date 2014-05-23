package edu.zsd.scomm.operations.copy

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.{SelectionInfo, DirectoriesPaneModel}
import edu.zsd.scomm.Domain._
import java.nio.file.Path

@Component
class CopyModel @Autowired()(directoriesPaneModel: DirectoriesPaneModel) {

  val source: Signal[SelectionInfo] = directoriesPaneModel.activeSelection

  val sourceParent: Signal[Path] = directoriesPaneModel.activeCurrentDir

  val destination: Signal[Path] = directoriesPaneModel.inactiveCurrentDir

}
