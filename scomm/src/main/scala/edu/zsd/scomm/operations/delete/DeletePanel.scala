package edu.zsd.scomm.operations.delete

import scala.swing.{Label, FlowPanel}
import org.springframework.stereotype.Component
import edu.zsd.scomm.view.EventButton
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.SelectionInfo
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.operations.CommandView

@Component
class DeletePanel @Autowired()(val model: DeleteModel) extends FlowPanel(FlowPanel.Alignment.Left)() with Observing with CommandView {

  val promptLabel = new Label("")
  val okButton = new EventButton("OK")
  val cancelButton = new EventButton("Cancel")

  contents ++= Seq(promptLabel, okButton, cancelButton)

  observe(model.toDelete) {
    selection: SelectionInfo =>
      promptLabel.text = selection match {
        case SelectionInfo.Nothing() => s"Nothing to delete!"
        case SelectionInfo.SingleFile(file) => s"Delete the file '${file.getFileName}'?"
        case SelectionInfo.SingleFolder(folder) => s"Delete the folder '${folder.getFileName}' and its contents?"
        case SelectionInfo.MultipleFiles(count, files) => s"Delete the selected $count files?"
        case SelectionInfo.MultipleFolders(count, folders) => s"Delete the selected $count folders and all their contents?"
        case SelectionInfo.FilesAndFolders(filesCount, foldersCount, paths) => s"Delete the selected $filesCount file(s) and $foldersCount folder(s) and all their contents?"
      }
  }

  override def reset() {
  }
}
