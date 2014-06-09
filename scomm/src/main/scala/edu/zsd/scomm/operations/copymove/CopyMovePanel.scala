package edu.zsd.scomm.operations.copymove

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import scala.swing.{Alignment, TextField, Label, FlowPanel}
import edu.zsd.scomm.view.EventButton
import java.awt.Dimension
import edu.zsd.scomm.model.SelectionInfo
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.operations.CommandView
import java.nio.file.Path

abstract sealed class CopyMovePanel(val model: CopyMoveModel) extends FlowPanel(FlowPanel.Alignment.Left)() with Observing with CommandView {

  val actionName: String
  // u.i. "copy" or "move"
  val actionNameCapital: String // u.i. "Copy" or "Move"

  private val promptLabel = new Label("")
  promptLabel.name = "copyMovePanel.prompt"
  private val destinationTextField = new TextField("")
  destinationTextField.preferredSize = new Dimension(200, 20)
  destinationTextField.horizontalAlignment = Alignment.Left
  destinationTextField.name = "copyMovePanel.destination"
  val okButton = new EventButton("OK")
  okButton.name = "copyMovePanel.ok"
  val cancelButton = new EventButton("Cancel")
  cancelButton.name = "copyMovePanel.cancel"

  contents ++= Seq(promptLabel, destinationTextField, okButton, cancelButton)

  override def reset() {
    destinationTextField.text = model.destination.now.toString
  }

  observe(model.source) {
    selection: SelectionInfo =>
      promptLabel.text = selection match {
        case SelectionInfo.Nothing() => s"Nothing to $actionName!"
        case SelectionInfo.SingleFile(file) => s"$actionNameCapital the file '${file.getFileName}' to:"
        case SelectionInfo.SingleFolder(folder) => s"$actionNameCapital the folder '${folder.getFileName}' and its contents to:"
        case SelectionInfo.MultipleFiles(count, files) => s"$actionNameCapital the selected $count files to:"
        case SelectionInfo.MultipleFolders(count, folders) => s"$actionNameCapital the selected $count folders and all their contents to:"
        case SelectionInfo.FilesAndFolders(filesCount, foldersCount, paths) => s"$actionNameCapital the selected $filesCount file(s) and $foldersCount folder(s) and all their contents to:"
      }
  }

  observe(model.destination) {
    destination: Path =>
      destinationTextField.text = destination.toString
  }

  def destination = destinationTextField.text
}

@Component
class CopyPanel @Autowired()(model: CopyMoveModel) extends CopyMovePanel(model) {
  override val actionName: String = "copy"
  override val actionNameCapital: String = "Copy"
  name = "copyPanel"
}

@Component
class MovePanel @Autowired()(model: CopyMoveModel) extends CopyMovePanel(model) {
  override val actionName: String = "move"
  override val actionNameCapital: String = "Move"
  name = "movePanel"
}
