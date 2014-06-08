package edu.zsd.scomm.operations.copymove

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.Domain._
import scala.util.continuations.suspendable
import java.nio.file.{Files, Paths, Path}
import edu.zsd.scomm.Utils._
import java.io.IOException
import edu.zsd.scomm.model.{DiskState, MainWindowModel}
import com.typesafe.scalalogging.slf4j.StrictLogging

@Component
class CopyController @Autowired()(val mainWindowView: MainWindowView,
                                  val mainWindowModel: MainWindowModel,
                                  val diskState: DiskState,
                                  val view: CopyPanel,
                                  val model: CopyMoveModel) extends Observing with StrictLogging {

  val copyLoop = Reactor.loop {

    self =>
      self awaitNext mainWindowView.commandButtons.copyButton()
      logger.debug("Loop triggered")

      // display the copy panel
      view.reset()
      mainWindowView.argumentsPanel() = Some(view.panel)

      self.abortOn(view.cancelButton()) {
        // at any point of time, abort on cancel
        self awaitNext view.okButton() // wait for the user to press he OK button

        val sourceDirs: Set[Path] = model.source.now.paths
        val destinationDir: Path = Paths.get(view.destination)
        execute(self, sourceDirs, destinationDir)
      }

      logger.debug("Loop done, resetting arguments panel")
      mainWindowView.argumentsPanel() = None
  }

  def execute(reactor: FlowOps, sourceDirs: Set[Path], destinationDir: Path): Unit@suspendable = {

    val overlapping: Set[Path] = sourceDirs.filter(dir => destinationDir.startsWith(dir))
    if (overlapping.nonEmpty) {
      mainWindowModel.status() = "You cannot copy a directory to its own subdirectory!"
    } else {

      sourceDirs.cps foreach {
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
              } catch {
                case e: IOException => logger.warn(e.toString, e)
              }
              reactor.pause
          }
      }

      diskState.refresh()

      mainWindowModel.status() = s"Successfully copied!"
    }
  }
}
