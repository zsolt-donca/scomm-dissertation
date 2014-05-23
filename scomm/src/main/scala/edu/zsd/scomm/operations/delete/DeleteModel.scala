package edu.zsd.scomm.operations.delete

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.{SelectionInfo, DirectoriesPaneModel}
import edu.zsd.scomm.Domain._

@Component
class DeleteModel @Autowired()(directoriesPaneModel: DirectoriesPaneModel) {

  val toDelete: Signal[SelectionInfo] = directoriesPaneModel.activeSelection

}
