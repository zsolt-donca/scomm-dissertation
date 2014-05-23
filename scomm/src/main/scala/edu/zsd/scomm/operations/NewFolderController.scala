package edu.zsd.scomm.operations

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.Domain
import edu.zsd.scomm.model.{DiskState, MainWindowModel, DirectoryListModel}
import java.nio.file.Path
import scala.util.continuations.suspendable


@Component
class NewFolderController @Autowired()(val mainWindowView: MainWindowView,
                                       val model: MainWindowModel,
                                       val newFolderPanel: NewFolderPanel,
                                       val diskState: DiskState) extends SimpleOperationController {

  override val commandView: BaseCommandView = newFolderPanel
  override val triggerEvent: Domain.Events[_] = mainWindowView.commandButtons.newFolderButton()
  override val continueEvent: Domain.Events[_] = newFolderPanel.okButton()
  override val abortEvent: Domain.Events[_] = newFolderPanel.cancelButton()

  override def execute(): Unit@suspendable = {
    val activeListModel: DirectoryListModel = model.directoriesPaneModel.activeList.now
    val currentDir: Path = activeListModel.currentDir.now

    val folderName: String = newFolderPanel.folderName.text
    val newFolderPath: Path = currentDir.resolve(folderName)

    val op = new NewFolderOperation(newFolderPath)
    val result = op.execute()
    result match {
      case op.Success() =>
        diskState.refresh()
        model.status() = s"Successfully created folder '$folderName'!"
        activeListModel.selectedPaths() = Set(newFolderPath)
      case op.FileAlreadyExists() =>
        model.status() = s"Folder '$folderName' already exists!"
      case op.GenericError(e) =>
        model.status() = "Error: " + e.getMessage
    }
  }

  val newFolderLoop = reactorLoop()

}
