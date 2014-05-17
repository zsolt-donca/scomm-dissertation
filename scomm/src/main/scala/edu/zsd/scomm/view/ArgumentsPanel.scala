package edu.zsd.scomm.view

import scala.swing.{BorderPanel, Panel}
import org.springframework.stereotype.Component
import scala.swing.BorderPanel.Position

@Component
class ArgumentsPanel extends BorderPanel {

  val statusPane = new StatusPane
  panel = statusPane

  def panel_=(panel: Panel): Unit = {
    _contents.clear()
    add(panel, Position.Center)
    revalidate()
  }

  def panel: Option[Panel] = {
    if (contents.isEmpty) None else Some(contents(0).asInstanceOf[Panel])
  }

  def resetPanel() {
    panel = statusPane
  }
}
