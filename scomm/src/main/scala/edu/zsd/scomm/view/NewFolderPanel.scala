package edu.zsd.scomm.view

import scala.swing.{FlowPanel, Label, TextField, Dimension, Alignment}
import org.springframework.stereotype.Component

@Component
class NewFolderPanel extends FlowPanel(FlowPanel.Alignment.Left)() {

  val prompt = new Label("New folder (directory)")
  val folderName = new TextField("")
  folderName.preferredSize = new Dimension(200, 20)
  folderName.horizontalAlignment = Alignment.Left
  val okButton = new EventButton("OK")

  contents ++= Seq(prompt, folderName, okButton)

  def reset() {
    folderName.text = ""
  }
}
