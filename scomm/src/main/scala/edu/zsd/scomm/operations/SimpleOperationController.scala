package edu.zsd.scomm.operations

import com.typesafe.scalalogging.slf4j.StrictLogging
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.view.MainWindowView

import scala.util.continuations.suspendable

trait SimpleOperationController extends Observing with StrictLogging {

  def mainWindowView: MainWindowView

  def commandView: CommandView

  def triggerEvent: Events[_]

  def continueEvent: Events[_] = commandView.okButton()

  def abortEvent: Events[_] = commandView.cancelButton() merge mainWindowView.commandButtons.anyEvent

  def reactorLoop(): Reactor = Reactor.loop {

    self =>
      self awaitNext triggerEvent
      logger.debug("Loop triggered")

      commandView.reset()
      mainWindowView.argumentsPanel() = Some(commandView.panel)

      self.pause
      self.abortOn(abortEvent) {
        self awaitNext continueEvent
        execute(self)
      }

      logger.debug("Loop done, resetting arguments panel")
      mainWindowView.argumentsPanel() = None
  }

  def execute(self: FlowOps): Unit@suspendable

}
