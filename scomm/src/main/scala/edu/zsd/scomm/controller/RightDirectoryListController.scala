package edu.zsd.scomm.controller

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.RightDirectoryListModel
import edu.zsd.scomm.view.RightDirectoryListView

@Component
class RightDirectoryListController @Autowired() (val model : RightDirectoryListModel,
                                                 val view : RightDirectoryListView) extends DirectoryListController(model, view) {


}
