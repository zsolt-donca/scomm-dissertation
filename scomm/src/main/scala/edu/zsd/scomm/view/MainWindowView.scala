package edu.zsd.scomm.view

import scala.swing.BorderPanel.Position
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.model.MainWindowModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.swing._
import javax.swing.border.BevelBorder

/**
 * Controller.
 */
@Component
class MainWindowView @Autowired()(val model: MainWindowModel,
                                  val directoriesPane: DirectoriesPaneView) extends MainFrame with Observing {

  menuBar = new MenuBar() {
    contents += new Menu("Files") {
      contents += new MenuItem("Quit")
    }
    contents += new Menu("Selection") {
      contents += new MenuItem("Select All")
      contents += new MenuItem("Unselect All")
      contents += new MenuItem("Invert Selection")
    }
    contents += new Menu("Help") {
      contents += new MenuItem("About")
    }
    contents += new Menu("System") {
      contents += new MenuItem(new Action("gc()") {
        override def apply(): Unit = System.gc()
      })
    }
  }

  val commandButtons = new FlowPanel(FlowPanel.Alignment.Left)() {
    val viewButton = new EventButton("View")
    contents += viewButton

    val editButton = new EventButton("Edit")
    contents += editButton

    val copyButton = new EventButton("Copy")
    copyButton.name = "copyButton"
    contents += copyButton

    val moveButton = new EventButton("Move")
    contents += moveButton

    val newFolderButton = new EventButton("New folder")
    newFolderButton.name = "newFolderButton"
    contents += newFolderButton

    val deleteButton = new EventButton("Delete")
    contents += deleteButton

    val infoButton = new EventButton("Info")
    infoButton.name = "infoButton"
    contents += infoButton

    val refreshButton = new EventButton("Refresh")
    refreshButton.name = "refreshButton"
    contents += refreshButton
  }

  val statusPanel = new BorderPanel() {
    val statusLabel = new Label
    statusLabel.horizontalAlignment = Alignment.Left
    add(statusLabel, Position.Center)
    border = new BevelBorder(BevelBorder.LOWERED)
  }

  val argumentsPanel = new BorderPanel {
    def update(panelOption: Option[Panel]): Unit = {
      _contents.clear()
      panelOption match {
        case Some(panel) =>
          add(panel, Position.Center)
        case None =>
      }
      revalidate()
    }

    def apply: Option[Panel] = {
      if (contents.isEmpty) None else Some(contents(0).asInstanceOf[Panel])
    }
  }

  contents = new BorderPanel() {
    add(directoriesPane, Position.Center)
    val southPanel = new BoxPanel(Orientation.Vertical) {
      contents ++= Seq(commandButtons, argumentsPanel, statusPanel)
    }
    add(southPanel, Position.South)
  }

  title = "Scala Commander"
  preferredSize = new Dimension(800, 500)
  pack()
  peer.setLocationRelativeTo(null)

  observe(model.status) {
    status => statusPanel.statusLabel.text = status
  }

}
