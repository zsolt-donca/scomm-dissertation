package edu.zsd.scomm.view

import scala.swing.Panel
import org.springframework.stereotype.Component

@Component
class ArgumentsPane extends Panel {

  //  preferredSize = new Dimension()

  def panel_=(panel: Panel): Unit = {
    _contents.clear()
    _contents += panel
    repaint()
  }

  def panel: Option[Panel] = {
    if (contents.isEmpty) None else Some(contents(0).asInstanceOf[Panel])
  }

  def clearPanel() {
    panel = new EmptyPane
  }

  clearPanel()
}
