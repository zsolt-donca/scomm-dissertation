package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{DiskState, MainWindowModel, DirectoryListModel}
import javax.swing.JOptionPane
import edu.zsd.scomm.Domain._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.typesafe.scalalogging.slf4j.StrictLogging
import edu.zsd.scomm.operations.delete.DeletePanel


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val deletePanel: DeletePanel)
  extends Observing with StrictLogging {

  observe(view.commandButtons.infoButton()) {
    _ =>
      val activeList: DirectoryListModel = view.directoriesPane.model.activeListModel.now
      val selectionInfo = activeList.selectionInfo.now
      val directories = selectionInfo.folders
      val files = selectionInfo.files
      val message = s"There are $directories directories and $files files selected in ${activeList.currentDir.now}"
      JOptionPane.showMessageDialog(null, message, "View", JOptionPane.INFORMATION_MESSAGE)
  }

  observe(view.commandButtons.refreshButton()) {
    _ =>
      diskState.refresh()
  }

}
