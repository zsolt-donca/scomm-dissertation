package edu.zsd.cpsdemo

import scala.util.continuations._
import scala.swing._
import scala.swing.BorderPanel.Position
import scala.swing.event.MouseClicked

object ContinuationsSwingApp extends SimpleSwingApplication {

  override def top: swing.Frame = new MainFrame {
    contents = new BorderPanel {
      val button = new Button("Next")
      add(button, Position.South)

      val textField = new TextArea(10, 40)
      add(textField, Position.Center)

      val label = new Label("Welcome to the demo app")
      add(label, Position.North)

      listenTo(button.mouse.clicks)
      reactions += {
        case MouseClicked(_, _, _, _, _) => eventHandler()
      }

      var eventHandler: (Unit => Unit) = { _ => run()}

      def run() {
        reset {
          val firstName = getResponse("What is your first name?")
          val lastName = getResponse("What is your last name?")
          label.text = s"Hello, $firstName $lastName!"
        }
      }

      def getResponse(prompt: String): String@cpsParam[Unit, Unit] = {
        label.text = prompt
        shift {
          k: (Unit => Unit) => {
            eventHandler = k
          }
        }
        textField.text
      }
    }
  }

}