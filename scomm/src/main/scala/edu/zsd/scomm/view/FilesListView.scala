package edu.zsd.scomm.view

import scala.swing.ListView
import javax.swing.{JList, AbstractListModel}

class FilesListView extends ListView[String] {

  private lazy val typedPeer: JList[String] = peer.asInstanceOf[JList[String]]

  private val model = new MyListModel

  var updating = false

  override def listData: Seq[String] = {
    super.listData
  }

  override def listData_=(items: Seq[String]): Unit = {
    val actualModel = typedPeer.getModel
    if (actualModel != model) typedPeer.setModel(model)

    model.items = items
  }

  private class MyListModel() extends AbstractListModel[String] {
    private var _items: Seq[String] = Seq.empty

    override def getSize: Int = items.size

    override def getElementAt(index: Int): String = items(index)

    def items: Seq[String] = _items

    def items_=(items: Seq[String]) {
      val oldSize = _items.size
      _items = Seq.empty
      if (oldSize > 0) {
        fireIntervalRemoved(FilesListView.this, 0, oldSize - 1)
      }

      _items = items
      if (_items.size > 0) {
        fireIntervalAdded(FilesListView.this, 0, _items.size - 1)
      }
    }
  }

}
