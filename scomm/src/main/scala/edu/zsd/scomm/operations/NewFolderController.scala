package edu.zsd.scomm.operations

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.domain._
import edu.zsd.scomm.model.{DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.{FileAlreadyExistsException, Files, Path}
import java.io.IOException
import scala.util.continuations.suspendable

@Component
class NewFolderController @Autowired()(val panel: NewFolderPanel,
                                       val model: MainWindowModel,
                                       val diskState: DiskState) {

  val cancelEvent: EventSource[Unit] = panel.cancelButton()

  def execute(flowOps: FlowOps): Unit@suspendable = {
    val activeList: DirectoryListModel = model.directoriesPaneModel.activeList.now
    val currentDir: Path = activeList.currentDir.now

    def tryCreateFolder(): Unit@suspendable = {
      flowOps awaitNext panel.okButton()
      val folderName: String = panel.folderName.text
      val newFolderPath: Path = currentDir.resolve(folderName)
      suspendable_try {
        try {
          Files.createDirectory(newFolderPath)
          diskState.refresh()
          model.status() = s"Successfully created folder '$folderName'!"
          activeList.selectedPaths() = Set(newFolderPath)
        } catch {
          case e: FileAlreadyExistsException =>
            model.status() = s"Folder '$folderName' already exists!"
            tryCreateFolder()
          case e: IOException =>
            model.status() = "Error: " + e.getMessage
            tryCreateFolder()
        }
      }
    }
    tryCreateFolder()
  }

}
