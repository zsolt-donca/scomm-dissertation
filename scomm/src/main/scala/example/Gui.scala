package example

import scala.swing.{FlowPanel, MainFrame}
import MySwingDomain._

object Gui extends ReactiveSwingApp {

  def top = new MainFrame {
    val button = new MyButton("test")
    contents = new FlowPanel {
      contents += button
    }
  }

  schedule {
    ConsoleObserver().obs()
  }
}

case class ConsoleObserver() extends Observing {
  def obs() {
    //    observe(Gui.button.actionPerformed)(_ => println("click"))
  }
}