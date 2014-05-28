package edu.zsd.scomm.operations.newfolder

import scala.swing.{FlowPanel, Label, TextField, Dimension, Alignment}
import org.springframework.stereotype.Component
import edu.zsd.scomm.view.EventButton
import com.typesafe.scalalogging.slf4j.StrictLogging
import edu.zsd.scomm.operations.CommandView

@Component
class NewFolderPanel extends FlowPanel(FlowPanel.Alignment.Left)() with CommandView with StrictLogging {

  name = "newFolder"

  val prompt = new Label("New folder (directory)")
  prompt.name = "newFolder.prompt"
  val folderName = new TextField("")
  folderName.name = "newFolder.folderName"
  folderName.preferredSize = new Dimension(200, 20)
  folderName.horizontalAlignment = Alignment.Left
  val okButton = new EventButton("OK")
  okButton.name = "newFolder.ok"
  val cancelButton = new EventButton("Cancel")
  cancelButton.name = "newFolder.cancel"

  contents ++= Seq(prompt, folderName, okButton, cancelButton)

  override def reset() {
    folderName.text = ""
  }
}
