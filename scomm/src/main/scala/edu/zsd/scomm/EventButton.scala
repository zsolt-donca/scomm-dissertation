package edu.zsd.scomm

import scala.swing.{Action, Button}
import edu.zsd.scomm.domain._

class EventButton(title: String) extends Button {

  private[this] val _actionEvent = EventSource[Unit]

  def actionEvents: Events[Unit] = _actionEvent

  action = new Action(title) {
    override def apply(): Unit = _actionEvent << Unit
  }

}
