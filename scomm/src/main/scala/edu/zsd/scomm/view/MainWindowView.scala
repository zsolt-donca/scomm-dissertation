package edu.zsd.scomm.view

import scala.swing.BorderPanel.Position
import edu.zsd.scomm.domain._
import edu.zsd.scomm.model.MainWindowModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.swing._

/**
 * Controller.
 */
@Component
class MainWindowView @Autowired() (val mainWindowModel : MainWindowModel,
                                   val directoriesPane : DirectoriesPaneView) extends MainFrame with Observing {

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
    contents += copyButton

    val moveButton = new EventButton("Move")
    contents += moveButton

    val newFolderButton = new EventButton("New folder")
    contents += newFolderButton

    val deleteButton = new EventButton("Delete")
    contents += deleteButton

    val infoButton = new EventButton("Info")
    infoButton.name = "infoButton"
    contents += infoButton
  }

  val commandParamsPanel = new FlowPanel(FlowPanel.Alignment.Leading)() {
    val button = new Button("?")
    contents += button
  }

  val southPanel = new BorderPanel {
    add(commandButtons, Position.North)
    add(commandParamsPanel, Position.South)
  }

  contents = new BorderPanel() {
    add(directoriesPane, Position.Center)
    add(southPanel, Position.South)
  }

  title = "Scala Commander"
  preferredSize = new Dimension(800, 500)
  pack()
  peer.setLocationRelativeTo(null)

}
