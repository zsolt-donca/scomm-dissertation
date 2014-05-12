package edu.zsd.scomm.controller

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.LeftDirectoryListModel
import edu.zsd.scomm.view.LeftDirectoryListView

@Component
class LeftDirectoryListController @Autowired() (val model : LeftDirectoryListModel,
                                                val view : LeftDirectoryListView) extends DirectoryListController(model, view) {


}
