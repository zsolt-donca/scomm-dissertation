package edu.zsd.scomm.actions

import scala.swing._
import scala.swing.BorderPanel.Position
import edu.zsd.scomm.view.EventButton


class NewFolder(owner : Window) {

  val dialog = new Dialog(owner) {
    val message = new Label("New folder (directory)")
    val folderName = new TextField("")
    val okButton = new EventButton("OK")
    contents = new BorderPanel {
      add(message, Position.North)
      add(folderName, Position.Center)
      add(okButton, Position.South)
    }
  }
}
