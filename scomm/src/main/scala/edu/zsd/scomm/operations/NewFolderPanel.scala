package edu.zsd.scomm.operations

import scala.swing.{FlowPanel, Label, TextField, Dimension, Alignment}
import org.springframework.stereotype.Component
import edu.zsd.scomm.view.EventButton
import com.typesafe.scalalogging.slf4j.StrictLogging

@Component
class NewFolderPanel extends FlowPanel(FlowPanel.Alignment.Left)() with BaseCommandView with StrictLogging {

  val prompt = new Label("New folder (directory)")
  val folderName = new TextField("")
  folderName.preferredSize = new Dimension(200, 20)
  folderName.horizontalAlignment = Alignment.Left
  val okButton = new EventButton("OK")
  val cancelButton = new EventButton("Cancel")

  contents ++= Seq(prompt, folderName, okButton, cancelButton)

  override def reset() {
    folderName.text = ""
  }
}
