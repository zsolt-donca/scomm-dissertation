package edu.zsd.scomm.operations

import scala.swing.{Label, FlowPanel}
import org.springframework.stereotype.Component
import edu.zsd.scomm.view.EventButton
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.model.{SelectionInfo, DirectoryListModel, MainWindowModel}

@Component
class DeletePanel @Autowired()(val model: MainWindowModel) extends FlowPanel(FlowPanel.Alignment.Left)() {

  val prompt = new Label("")
  val okButton = new EventButton("OK")
  val cancelButton = new EventButton("Cancel")

  contents ++= Seq(prompt, okButton, cancelButton)

  def reset() {
    val directoryListModel: DirectoryListModel = model.directoriesPaneModel.activeList.now
    val selectionInfo: SelectionInfo = directoryListModel.selectionInfo.now

    prompt.text = selectionInfo match {
      case SelectionInfo.SingleFile(file) => s"Are you sure you want to delete the file '${file.getFileName}'?"
      case SelectionInfo.SingleFolder(folder) => s"Are you sure you want to delete the folder '${folder.getFileName}' and its contents?"
      case SelectionInfo.MultipleFiles(count, files) => s"Are you sure you want to delete the $count selected files?"
      case SelectionInfo.MultipleFolders(count, folders) => s"Are you sure you want to delete the $count selected folders and all their contents?"
      case SelectionInfo.FilesAndFolders(filesCount, foldersCount, paths) => s"Are you sure you want to delete the selected $filesCount file(s) and $foldersCount folder(s) and all their contents?"
      case _ => s"Are you sure you want to delete the ${selectionInfo.paths.size} items?"
    }

  }

}
