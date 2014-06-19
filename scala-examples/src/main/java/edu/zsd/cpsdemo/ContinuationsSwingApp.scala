package edu.zsd.cpsdemo

import scala.swing._
import scala.util.continuations._

object ContinuationsSwingApp extends SimpleSwingApplication {

  var continue: (Unit => Unit) = { _ => run()}

  def run() {
    reset {
      val first = ask("What is your first name?")
      val last = ask("What is your last name?")
      label.text = s"Hello, $first $last!"
    }
  }

  def ask(prompt: String): String@cpsParam[Unit, Unit] = {
    label.text = prompt
    shift {
      k: (Unit => Unit) => {
        continue = k
      }
    }
    textField.text
  }

  val textField = new TextArea(10, 40)
  val label = new Label("Welcome to the demo app")
  val button = new Button(new Action("Next") {
    override def apply(): Unit = continue()
  })

  override def top: swing.Frame = new MainFrame {
    contents = new BorderPanel {
      add(label, BorderPanel.Position.North)
      add(textField, BorderPanel.Position.Center)
      add(button, BorderPanel.Position.South)
    }
  }
}