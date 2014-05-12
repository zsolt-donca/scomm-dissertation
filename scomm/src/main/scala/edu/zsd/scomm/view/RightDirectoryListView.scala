package edu.zsd.scomm.view

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.{RightDirectoryListModel, LeftDirectoryListModel}

@Component
class RightDirectoryListView @Autowired() (val model : RightDirectoryListModel) extends DirectoryListView("directoriesPane.right", model) {

}
