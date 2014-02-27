package edu.zsd.scomm

import scala.swing._
import java.io.File
import scala.collection.mutable
import scala.swing.event.Event
import scala.react.{Var, Observing}

class DirectoryListView(val initDir: File) extends ListView[String] with Observing {

  var currentDir : Var[File] = new Var[File](initDir)
  listData = fileContents(initDir)

  listenTo(mouse.clicks)
  reactions += {
    case scala.swing.event.MouseClicked(_, _, _, 2, _) =>
      if (!DirectoryListView.this.selection.items.isEmpty) {
        val selectedItem: String = DirectoryListView.this.selection.items(0)
        val newFile: File = new File(currentDir.now, selectedItem).getCanonicalFile
        listData = fileContents(newFile)
        currentDir.update(newFile)
      }
  }

  def fileContents(file: File): Seq[String] = {
    val contents = file.list()
    val arr: Seq[String] = if (contents != null) contents else List()
    List("..") ++ arr
  }

}
