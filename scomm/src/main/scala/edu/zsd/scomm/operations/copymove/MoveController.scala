package edu.zsd.scomm.operations.copymove

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.model.{DiskState, MainWindowModel}
import edu.zsd.scomm.operations.{CommandView, SimpleOperationController}
import edu.zsd.scomm.Domain._
import scala.util.continuations.suspendable
import java.nio.file.{Files, Path}
import java.io.IOException
import com.typesafe.scalalogging.slf4j.StrictLogging

@Component
class MoveController @Autowired()(val mainWindowView: MainWindowView,
                                  val mainWindowModel: MainWindowModel,
                                  val diskState: DiskState,
                                  val view: MovePanel,
                                  val model: CopyMoveModel) extends SimpleOperationController with StrictLogging {

  override val commandView: CommandView = view
  override val triggerEvent: Events[_] = mainWindowView.commandButtons.moveButton()

  override def execute(self: FlowOps): Unit@suspendable = {

    val sourceParent: Path = model.sourceParent.now
    val sourcePaths = model.source.now.paths
    val destination = model.destination.now

    execute(self, sourceParent, sourcePaths, destination)

  }

  def execute(self: FlowOps, sourceParent: Path, sourcePaths: Set[Path], destination: Path): Unit@suspendable = {
    sourcePaths.foreach {
      sourcePath: Path =>
        try {
          val destinationPath = destination.resolve(sourceParent.relativize(sourcePath))
          Files.move(sourcePath, destinationPath)
          diskState.refresh()
        }
        catch {
          case e: IOException =>
            logger.error(e.toString, e)
        }
    }
  }

  val moveLoop = reactorLoop()
}
