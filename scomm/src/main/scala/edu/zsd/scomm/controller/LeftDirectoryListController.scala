package edu.zsd.scomm.controller

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.LeftDirectoryListModel
import edu.zsd.scomm.view.LeftDirectoryListView
import edu.zsd.scomm.operations.copymove.{MoveController, CopyController}

@Component
class LeftDirectoryListController @Autowired()(model: LeftDirectoryListModel,
                                               view: LeftDirectoryListView,
                                               copyController: CopyController,
                                               moveController: MoveController) extends DirectoryListController(model, view, copyController, moveController) {


}
