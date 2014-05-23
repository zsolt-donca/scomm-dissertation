package edu.zsd.scomm.operations.newfolder

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.model.{DiskState, MainWindowModel}
import java.nio.file.{FileAlreadyExistsException, Files, Path}
import scala.util.continuations.suspendable
import java.io.IOException
import edu.zsd.scomm.operations.{CommandView, SimpleOperationController}


@Component
class NewFolderController @Autowired()(val mainWindowView: MainWindowView,
                                       val mainWindowModel: MainWindowModel,
                                       val newFolderPanel: NewFolderPanel,
                                       val model: NewFolderModel,
                                       val diskState: DiskState) extends SimpleOperationController {

  override val commandView: CommandView = newFolderPanel
  override val triggerEvent: Events[_] = mainWindowView.commandButtons.newFolderButton()

  override def execute(self: FlowOps): Unit@suspendable = {
    val currentDir: Path = model.destinationDir.now

    val folderName: String = newFolderPanel.folderName.text
    val newFolderPath: Path = currentDir.resolve(folderName)

    try {
      Files.createDirectory(newFolderPath)
      diskState.refresh()
      mainWindowModel.status() = s"Successfully created folder '$folderName'!"
      model.destinationListModel.now.selectedPaths() = Set(newFolderPath)
    } catch {
      case e: FileAlreadyExistsException =>
        mainWindowModel.status() = s"Folder '$folderName' already exists!"
      case e: IOException =>
        mainWindowModel.status() = "Error: " + e.getMessage
    }
  }

  val newFolderLoop = reactorLoop()

}
