package edu.zsd.scomm.operations.delete

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{DiskState, MainWindowModel}
import edu.zsd.scomm.Domain._
import scala.util.continuations.suspendable
import edu.zsd.scomm.Utils._
import java.nio.file.{Path, Files}
import java.io.IOException
import edu.zsd.scomm.operations.{CommandView, SimpleOperationController}

@Component
class DeleteController @Autowired()(val mainWindowView: MainWindowView,
                                    val mainWindowModel: MainWindowModel,
                                    val diskState: DiskState,
                                    val deletePanel: DeletePanel,
                                    val model: DeleteModel) extends SimpleOperationController {
  override val commandView: CommandView = deletePanel
  override val triggerEvent: Events[_] = mainWindowView.commandButtons.deleteButton()

  override def execute(self: FlowOps): Unit@suspendable = {

    val pathsToDelete: Set[Path] = model.toDelete.now.paths
    walkPathsPostOrder(pathsToDelete) {
      path =>
        try {
          mainWindowModel.status() = s"Deleting '$path'..."
          Files.setAttribute(path, "dos:readonly", false)
          Files.delete(path)
          self.pause
        } catch {
          case e: IOException => logger.error(e.toString, e)
        }
    }
    diskState.refresh()

    mainWindowModel.status() = s"Successfully deleted!"
  }

  val deleteLoop = reactorLoop()
}
