package edu.zsd.scomm.controller

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.RightDirectoryListModel
import edu.zsd.scomm.view.RightDirectoryListView
import edu.zsd.scomm.operations.copymove.{MoveController, CopyController}

@Component
class RightDirectoryListController @Autowired()(model: RightDirectoryListModel,
                                                view: RightDirectoryListView,
                                                copyController: CopyController,
                                                moveController: MoveController) extends DirectoryListController(model, view, copyController, moveController) {


}
