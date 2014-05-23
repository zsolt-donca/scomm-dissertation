package edu.zsd.scomm.operations

import scala.swing.Panel

trait BaseCommandView {

  self: Panel =>

  def reset(): Unit

  def panel: Panel = self

}
