package edu.zsd.scomm.controller

import edu.zsd.scomm.model.DirectoryListModel
import edu.zsd.scomm.view.DirectoryListView
import scala.swing.event.{Key, ListSelectionChanged}
import com.typesafe.scalalogging.slf4j.StrictLogging
import javax.swing.TransferHandler.TransferSupport
import java.awt.datatransfer.{Transferable, DataFlavor}
import scala.swing.event.KeyPressed
import scala.swing.event.MouseClicked
import scala.collection.JavaConverters._
import java.io.File
import edu.zsd.scomm.operations.copymove.{MoveController, CopyController}
import edu.zsd.scomm.Domain._
import javax.swing.{JComponent, TransferHandler, DropMode}
import edu.zsd.scomm.Utils.unit


/**
 * Controller.
 *
 */
abstract class DirectoryListController(val model: DirectoryListModel,
                                       val view: DirectoryListView,
                                       val copyController: CopyController,
                                       val moveController: MoveController) extends scala.swing.Reactor with StrictLogging {

  listenTo(view.listView.mouse.clicks, view.listView.selection, view.listView.keys)
  reactions += {
    case MouseClicked(_, _, _, 2, _) =>
      val leadIndex = view.listView.selection.leadIndex
      logger.debug(s"Double click, going to $leadIndex")
      model.goToIndex << leadIndex
    case ListSelectionChanged(_, _, false) =>
      if (!view.listView.updating) {
        val selection = view.listView.selection.indices.toSet
        logger.debug(s"List selection changed, setting model indices to $selection")
        model.selectIndices << selection
      }
    case KeyPressed(_, Key.Enter, _, _) =>
      val leadIndex = view.listView.selection.leadIndex
      logger.debug(s"Enter pressed, going to $leadIndex")
      model.goToIndex << leadIndex
    case KeyPressed(_, Key.BackSpace, _, _) =>
      logger.debug("Backspace pressed, going to parent")
      model.goToParent << Unit
  }

  var list = view.listView.peer
  list.setDragEnabled(true)
  list.setDropMode(DropMode.ON)

  val myTransferHandler = new TransferHandler() {

    private val activeReactors = mutable.Set[Reactor]()

    override def canImport(info: TransferSupport): Boolean = {
      val supported: Boolean = info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
      supported
    }

    override def importData(info: TransferSupport): Boolean = {

      logger.debug("Importing data.....")

      if (!info.isDrop) {
        logger.warn("Importing data by other means than drop?")
        return false
      }

      if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        logger.warn("Trying to drop something that is not coming from our directory lists?")
        return false
      }

      try {
        val transferable: Transferable = info.getTransferable
        val data = transferable.getTransferData(DataFlavor.javaFileListFlavor).asInstanceOf[java.util.List[File]]
        val sourcePaths = data.asScala.map(_.toPath).toSet
        val sourceParent = sourcePaths.toSeq(0).getParent

        info.getDropAction match {
          case TransferHandler.COPY =>
            logger.info(s"Needs to copy these paths: $sourcePaths")

            schedule {
              val copyFlow: Reactor = Reactor.flow {
                self =>
                  copyController.execute(self, sourceParent, sourcePaths, model.currentDir.now)
                  unit()
              }
              activeReactors += copyFlow
              unit()
            }
            true
          case TransferHandler.MOVE =>
            logger.info(s"Needs to move these paths: $sourcePaths")
            true
          case _ =>
            false
        }
      } catch {
        case e: Exception =>
          logger.error(e.toString, e)
          false
      }
    }

    override def getSourceActions(c: JComponent): Int = TransferHandler.COPY_OR_MOVE

    protected override def createTransferable(c: JComponent): Transferable = {
      logger.debug("Exporting data.....")
      new Transferable() {

        import DataFlavor._

        private val paths = view.listView.selection.items.map(_.path)
        private lazy val string = paths.map(_.toString).mkString(" ")
        private lazy val files: java.util.List[File] = new java.util.ArrayList(paths.map(_.toFile).asJavaCollection)

        override def getTransferDataFlavors: Array[DataFlavor] = Array(stringFlavor, javaFileListFlavor)

        override def getTransferData(flavor: DataFlavor): AnyRef =
          flavor match {
            case `stringFlavor` => string
            case `javaFileListFlavor` => files
            case _ => null
          }

        override def isDataFlavorSupported(flavor: DataFlavor): Boolean = Set(stringFlavor, javaFileListFlavor)(flavor)
      }
    }
  }

  list.setTransferHandler(myTransferHandler)
}
