package edu.zsd.scomm.view

import javax.swing.border.BevelBorder
import javax.swing.{JFrame, JOptionPane}

import edu.zsd.scomm.Domain._
import edu.zsd.scomm.model.MainWindowModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.swing.BorderPanel.Position
import scala.swing._

/**
 * Controller.
 */
@Component
class MainWindowView @Autowired()(val model: MainWindowModel,
                                  val directoriesPane: DirectoriesPaneView) extends MainFrame with Observing {

  menuBar = new MenuBar() {
    contents += new Menu("Files") {
      contents += new MenuItem(new Action("Quit") {
        override def apply(): Unit = System.exit(0)
      })
    }
    contents += new Menu("Selection") {
      contents += new MenuItem(new NotImplemented("Select All"))
      contents += new MenuItem(new NotImplemented("Unselect All"))
      contents += new MenuItem(new NotImplemented("Invert Selection"))
    }
    contents += new Menu("Help") {
      contents += new MenuItem(new NotImplemented("About"))
    }
    contents += new Menu("System") {
      contents += new MenuItem(new Action("gc()") {
        override def apply(): Unit = System.gc()
      })
    }
  }

  val commandButtons = new FlowPanel(FlowPanel.Alignment.Left)() {
    contents += new Button(new NotImplemented("View"))

    contents += new Button(new NotImplemented("Edit"))

    val copyButton = new EventButton("Copy")
    copyButton.name = "copyButton"
    contents += copyButton

    val moveButton = new EventButton("Move")
    moveButton.name = "moveButton"
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

    val anyEvent: Events[Any] = {
      copyButton() merge moveButton() merge newFolderButton() merge deleteButton()
    }
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
      repaint()
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

  class NotImplemented(title: String) extends Action(title) {
    override def apply(): Unit = {
      val window: JFrame = MainWindowView.this.peer
      JOptionPane.showMessageDialog(window, "Not implemented!", "Error", JOptionPane.ERROR_MESSAGE)
    }
  }

}
