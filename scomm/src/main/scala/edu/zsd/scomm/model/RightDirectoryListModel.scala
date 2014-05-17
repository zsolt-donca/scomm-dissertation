package edu.zsd.scomm.model

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.AppParams

@Component
class RightDirectoryListModel @Autowired()(val appParams: AppParams,
                                           val diskState: DiskState) extends DirectoryListModel(appParams.initRightDir, diskState) {

 }
