package edu.zsd.scomm.view

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.LeftDirectoryListModel

@Component
class LeftDirectoryListView @Autowired() (val model : LeftDirectoryListModel) extends DirectoryListView("directoriesPane.left", model) {

}
