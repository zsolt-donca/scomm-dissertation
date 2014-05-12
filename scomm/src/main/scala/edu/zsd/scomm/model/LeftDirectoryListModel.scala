package edu.zsd.scomm.model

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.AppParams

@Component
class LeftDirectoryListModel @Autowired() (val appParams : AppParams) extends DirectoryListModel(appParams.initLeftDir) {

}
