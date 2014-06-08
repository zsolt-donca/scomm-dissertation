package edu.zsd.scomm.operations.newfolder

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.{DirectoryListModel, DirectoriesPaneModel}
import edu.zsd.scomm.Domain._
import java.nio.file.Path

@Component
class NewFolderModel @Autowired()(directoriesPaneModel: DirectoriesPaneModel) {

  val destinationDir: Signal[Path] = directoriesPaneModel.activeCurrentDir

  val destinationListModel: Signal[DirectoryListModel] = directoriesPaneModel.activeList
}
