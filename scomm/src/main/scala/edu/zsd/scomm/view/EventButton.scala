package edu.zsd.scomm.view

import scala.swing.{Action, Button}
import edu.zsd.scomm.domain._

class EventButton(title: String) extends Button {

  private[this] val _actionEvent = EventSource[this.type]

  def apply(): Events[this.type] = _actionEvent

  action = new Action(title) {
    override def apply(): Unit = _actionEvent << EventButton.this
  }

}
