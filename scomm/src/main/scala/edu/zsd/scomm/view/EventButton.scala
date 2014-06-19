package edu.zsd.scomm.view

import edu.zsd.scomm.Domain._

import scala.swing.{Action, Button}

class EventButton(title: String) extends Button {

  private[this] val _actionEvent = EventSource[this.type]

  def apply(): Events[Any] = _actionEvent

  action = new Action(title) {
    override def apply(): Unit = _actionEvent << EventButton.this
  }

}
