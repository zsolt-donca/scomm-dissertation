package edu.zsd.scomm.operations.copymove

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
import edu.zsd.scomm.operations.{CommandView, SimpleOperationController}

@Component
class CopyController @Autowired()(val mainWindowView: MainWindowView,
                                  val mainWindowModel: MainWindowModel,
                                  val diskState: DiskState,
                                  val view: CopyPanel,
                                  val model: CopyMoveModel) extends SimpleOperationController {
  override val commandView: CommandView = view
  override val triggerEvent: Domain.Events[_] = mainWindowView.commandButtons.copyButton()

  override def execute(self: FlowOps): Unit@suspendable = {

    val sourceDirs: Set[Path] = model.source.now.paths
    val destinationDir: Path = Paths.get(view.destination)

    execute(self, sourceDirs, destinationDir)
  }

  def execute(self: FlowOps, sourceDirs: Set[Path], destinationDir: Path): Unit@suspendable = {

    val overlapping: Set[Path] = sourceDirs.filter(dir => destinationDir.startsWith(dir))
    if (overlapping.nonEmpty) {
      mainWindowModel.status() = "You cannot copy a directory to its own subdirectory!"
    } else {

      cps_foreach(sourceDirs) {
        sourceDir =>
          val sourceParent = sourceDir.getParent
          walkPathsPreOrder(Seq(sourceDir)) {
            sourcePath =>
              try {
                val destinationPath = destinationDir.resolve(sourceParent.relativize(sourcePath))

                if (Files.isRegularFile(sourcePath)) {
                  mainWindowModel.status() = s"Copying '$sourcePath' to '${destinationPath.getParent}'..."
                  Files.copy(sourcePath, destinationPath)
                } else if (Files.isDirectory(sourcePath)) {
                  if (!Files.exists(destinationPath)) {
                    mainWindowModel.status() = s"Creating directory '$destinationPath'..."
                    Files.createDirectory(destinationPath)
                  } else {
                    mainWindowModel.status() = s"Directory already exists '$destinationPath'..."
                  }
                }
                self.pause
              } catch {
                case e: IOException => e.printStackTrace()
              }
          }
      }

      walkPathsPreOrder(sourceDirs) {
        sourcePath =>
      }
      diskState.refresh()

      mainWindowModel.status() = s"Successfully copied!"
    }
  }

  val copyLoop = reactorLoop()
}
