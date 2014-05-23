package edu.zsd.scomm.operations.copy

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import scala.swing.{Alignment, TextField, Label, FlowPanel}
import edu.zsd.scomm.view.EventButton
import java.awt.Dimension
import edu.zsd.scomm.model.SelectionInfo
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.operations.CommandView
import java.nio.file.Path


@Component
class CopyPanel @Autowired()(val model: CopyModel) extends FlowPanel(FlowPanel.Alignment.Left)() with Observing with CommandView {

  val promptLabel = new Label("")
  val destinationTextField = new TextField("")
  destinationTextField.preferredSize = new Dimension(200, 20)
  destinationTextField.horizontalAlignment = Alignment.Left
  val okButton = new EventButton("OK")
  val cancelButton = new EventButton("Cancel")

  contents ++= Seq(promptLabel, destinationTextField, okButton, cancelButton)

  override def reset() {
    destinationTextField.text = model.destination.now.toString
  }

  observe(model.source) {
    selection: SelectionInfo =>
      promptLabel.text = selection match {
        case SelectionInfo.Nothing() => s"Nothing to copy!"
        case SelectionInfo.SingleFile(file) => s"Copy the file '${file.getFileName}' to:"
        case SelectionInfo.SingleFolder(folder) => s"Copy the folder '${folder.getFileName}' and its contents to:"
        case SelectionInfo.MultipleFiles(count, files) => s"Copy the selected $count files to:"
        case SelectionInfo.MultipleFolders(count, folders) => s"Copy the selected $count folders and all their contents to:"
        case SelectionInfo.FilesAndFolders(filesCount, foldersCount, paths) => s"Copy the selected $filesCount file(s) and $foldersCount folder(s) and all their contents to:"
      }
  }

  observe(model.destination) {
    destination: Path =>
      destinationTextField.text = destination.toString
  }
}
