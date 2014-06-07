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
import javax.swing.{JList, JComponent, TransferHandler, DropMode}
import edu.zsd.scomm.Utils.unit
import java.nio.file.Paths


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
    case MouseClicked(_, _, _, 2, _) => // triggered by `mouse.clicks'
      val leadIndex = view.listView.selection.leadIndex
      logger.debug(s"Double click, going to $leadIndex")
      model.goToIndex << leadIndex
    case ListSelectionChanged(_, _, false) => // triggered by `selection'
      if (!view.listView.updating) {
        val selection = view.listView.selection.indices.toSet
        logger.debug(s"List selection changed, setting model indices to $selection")
        model.selectIndices << selection
      }
    case KeyPressed(_, Key.Enter, _, _) => // triggered by `key'
      val leadIndex = view.listView.selection.leadIndex
      logger.debug(s"Enter pressed, going to $leadIndex")
      model.goToIndex << leadIndex
    case KeyPressed(_, Key.BackSpace, _, _) => // triggered by `key'
      logger.debug("Backspace pressed, going to parent")
      model.goToParent << Unit
  }

  var list = view.listView.peer
  list.setDragEnabled(true)
  list.setDropMode(DropMode.ON)
  list.setTransferHandler(new TransferHandler() {

    private val activeReactors = scala.collection.mutable.Set[Reactor]()

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

        val dropLocation: JList.DropLocation = info.getDropLocation.asInstanceOf[JList.DropLocation]
        val destinationPath = if (dropLocation.isInsert || dropLocation.getIndex < 0) {
          Paths.get(view.currentDirLabel.text) // current directory
        } else {
          view.listView.listData(dropLocation.getIndex).path
        }

        val dropAction: Int = info.getDropAction
        if (dropAction == TransferHandler.MOVE || dropAction == TransferHandler.COPY) {
          schedule {
            lazy val copyFlow: Reactor = Reactor.flow {
              self =>
                dropAction match {
                  case TransferHandler.MOVE => moveController.execute(self, sourcePaths, destinationPath)
                  case TransferHandler.COPY => copyController.execute(self, sourcePaths, destinationPath)
                }
                activeReactors -= copyFlow
                unit()
            }
            activeReactors += copyFlow
            unit()
          }
          true
        } else {
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

        private val paths = view.listView.selection.items.filter(_.name != "..").map(_.path)
        private lazy val string = paths.map(_.toString).mkString("\n")
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
  })
}
