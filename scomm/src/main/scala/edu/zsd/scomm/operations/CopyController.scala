package edu.zsd.scomm.operations

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.Domain
import edu.zsd.scomm.Domain._
import scala.util.continuations.suspendable
import java.nio.file.{Files, Paths, Path}
import edu.zsd.scomm.Utils._
import java.io.IOException
import edu.zsd.scomm.model.{DiskState, MainWindowModel}

@Component
class CopyController @Autowired()(val mainWindowView: MainWindowView,
                                  val mainWindowModel: MainWindowModel,
                                  val diskState: DiskState,
                                  val model: CopyModel,
                                  val view: CopyPanel) extends SimpleOperationController {
  override val commandView: BaseCommandView = view
  override val triggerEvent: Domain.Events[_] = mainWindowView.commandButtons.copyButton()
  override val abortEvent: Domain.Events[_] = view.cancelButton()
  override val continueEvent: Domain.Events[_] = view.okButton()

  override def execute(self: FlowOps): Unit@suspendable = {

    val sourceDir: Path = model.sourceDir.now
    val sourceDirs: Set[Path] = model.selection.now.paths
    val destinationDir: Path = Paths.get(view.destinationTextField.text)

    val overlapping: Set[Path] = sourceDirs.filter(dir => destinationDir.startsWith(dir))
    if (overlapping.nonEmpty) {
      mainWindowModel.status() = "You cannot copy a directory to its own subdirectory!"
    } else {

      walkPathsPreOrder(sourceDirs) {
        sourcePath =>
          try {
            val destinationPath = destinationDir.resolve(sourceDir.relativize(sourcePath))

            if (Files.isRegularFile(sourcePath)) {
              mainWindowModel.status() = s"Copying '$sourcePath' to '${destinationPath.getParent}'..."
              Files.copy(sourcePath, destinationPath)
            } else if (Files.isDirectory(sourcePath)) {
              mainWindowModel.status() = s"Creating directory '$destinationPath'..."
              Files.createDirectory(destinationPath)
            }
            self.pause
          } catch {
            case e: IOException => e.printStackTrace()
          }
      }
      diskState.refresh()

      mainWindowModel.status() = s"Successfully copied!"
    }
  }

  val copyLoop = reactorLoop()
}
