package example

import java.awt.event.{ActionEvent, ActionListener}
import MySwingDomain._
import scala.swing.{AbstractButton, Action, Button}

class MyButton(text: String) extends Button(text) with HasAction {
}

trait HasAction extends Observing {
  this: AbstractButton =>

  def getAction = peer.getAction

  def addActionListener(a: ActionListener) = peer.addActionListener(a)

  def removeActionListener(a: ActionListener) = peer.removeActionListener(a)

  val actionPerformed: Events[Action] =
    new EventSource[Action](MySwingDomain.owner) {
      source =>
      addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) = {
          source emit getAction
        }
      })
    }
}