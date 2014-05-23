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
                                       val diskState: DiskState) extends BaseCommandController(mainWindowView) {

  override val commandView: BaseCommandView = newFolderPanel
  override val triggerEvent: Domain.Events[Any] = mainWindowView.commandButtons.newFolderButton()
  override val continueEvent: Domain.Events[Any] = newFolderPanel.okButton()
  override val abortEvent: Domain.Events[Any] = newFolderPanel.cancelButton()

  override def execute(): Unit@suspendable = {
    val activeListModel: DirectoryListModel = model.directoriesPaneModel.activeList.now
    val currentDir: Path = activeListModel.currentDir.now

    val folderName: String = newFolderPanel.folderName.text
    val newFolderPath: Path = currentDir.resolve(folderName)

    val op = new NewFolderOperation(newFolderPath)
    val result = op.execute()
    result match {
      case NewFolderOperation.Success() =>
        diskState.refresh()
        model.status() = s"Successfully created folder '$folderName'!"
        activeListModel.selectedPaths() = Set(newFolderPath)
      case NewFolderOperation.FileAlreadyExists() =>
        model.status() = s"Folder '$folderName' already exists!"
      case NewFolderOperation.GenericError(e) =>
        model.status() = "Error: " + e.getMessage
    }
  }

  val newFolderLoop = reactorLoop()

}
