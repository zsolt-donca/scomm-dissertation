package edu.zsd.scomm.operations

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import scala.swing.{Alignment, TextField, Label, FlowPanel}
import edu.zsd.scomm.view.EventButton
import java.awt.Dimension
import edu.zsd.scomm.model.{MainWindowModel, DirectoryListModel, SelectionInfo}

@Component
class CopyPanel @Autowired()(val model: MainWindowModel) extends FlowPanel(FlowPanel.Alignment.Left)() {

  val prompt = new Label("")
  val destination = new TextField("")
  destination.preferredSize = new Dimension(200, 20)
  destination.horizontalAlignment = Alignment.Left
  val okButton = new EventButton("OK")
  val cancelButton = new EventButton("Cancel")

  contents ++= Seq(prompt, destination, okButton, cancelButton)

  def reset() {
    val directoryListModel: DirectoryListModel = model.directoriesPaneModel.activeList.now
    val selectionInfo: SelectionInfo = directoryListModel.selectionInfo.now

    prompt.text = selectionInfo match {
      case SelectionInfo.Nothing() => s"Nothing to copy!"
      case SelectionInfo.SingleFile(file) => s"Copy the file '${file.getFileName}' to:"
      case SelectionInfo.SingleFolder(folder) => s"Copy the folder '${folder.getFileName}' and its contents to:"
      case SelectionInfo.MultipleFiles(count, files) => s"Copy the selected $count files to:"
      case SelectionInfo.MultipleFolders(count, folders) => s"Copy the selected $count folders and all their contents to:"
      case SelectionInfo.FilesAndFolders(filesCount, foldersCount, paths) => s"Copy the selected $filesCount file(s) and $foldersCount folder(s) and all their contents to:"
    }

    destination.text = model.directoriesPaneModel.inactiveList.now.currentDir.now.toString
  }
}
