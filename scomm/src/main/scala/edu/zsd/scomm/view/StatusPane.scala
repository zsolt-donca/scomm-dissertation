package edu.zsd.scomm.view

import scala.swing.{BorderPanel, Label}
import scala.swing.BorderPanel.Position

class StatusPane extends BorderPanel {

  private val _status = new Label("Ready")
  add(_status, Position.West)

  def status: String = _status.text

  def status_=(status: String) = _status.text = status
}
