package edu.zsd.scomm.controller

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{SelectionInfo, DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.Files
import javax.swing.JOptionPane
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.Utils._
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import edu.zsd.scomm.operations.{CopyPanel, DeletePanel, NewFolderPanel}
import com.typesafe.scalalogging.slf4j.StrictLogging


@Component
class MainWindowController @Autowired()(val model: MainWindowModel,
                                        val view: MainWindowView,
                                        val diskState: DiskState,
                                        val newFolderPanel: NewFolderPanel,
                                        val deletePanel: DeletePanel,
                                        val copyPanel: CopyPanel,
                                        val directoriesPaneController: DirectoriesPaneController)
  extends Observing with StrictLogging {

  val infoActionReactor = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.infoButton()
      val activeList: DirectoryListModel = view.directoriesPane.model.activeList.now
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

  val deleteLoop = Reactor.loop {
    self =>
      self awaitNext view.commandButtons.deleteButton()

      deletePanel.reset()
      view.argumentsPanel() = Some(deletePanel)

      self.abortOn(deletePanel.cancelButton()) {
        val selectionInfo: SelectionInfo = model.directoriesPaneModel.activeList.now.selectionInfo.now

        self awaitNext deletePanel.okButton()
        walkPathsPostOrder(selectionInfo.paths) {
          path =>
            try {
              model.status() = s"Deleting '$path'..."
              Files.setAttribute(path, "dos:readonly", false)
              Files.delete(path)
              self.pause
            } catch {
              case e: IOException => e.printStackTrace()
            }
        }
        diskState.refresh()

        model.status() = s"Successfully deleted!"
      }

      view.argumentsPanel() = None
  }

}
