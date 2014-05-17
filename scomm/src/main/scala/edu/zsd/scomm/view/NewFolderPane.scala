package edu.zsd.scomm.view

import scala.swing._


class NewFolderPane extends FlowPanel(FlowPanel.Alignment.Leading)() {

  val prompt = new Label("New folder (directory)")
  val folderName = new TextField("")
  folderName.preferredSize = new Dimension(200, 20)
  folderName.horizontalAlignment = Alignment.Left
  val okButton = new EventButton("OK")
  val message = new Label("")

  contents ++= Seq(prompt, folderName, okButton, message)
}
