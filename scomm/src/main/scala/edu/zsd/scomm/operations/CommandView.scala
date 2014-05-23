package edu.zsd.scomm.operations

import scala.swing.Panel
import edu.zsd.scomm.view.EventButton

trait CommandView {

  self: Panel =>

  val okButton: EventButton

  val cancelButton: EventButton

  def reset(): Unit

  def panel: Panel = self

}
