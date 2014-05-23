package edu.zsd.scomm.operations

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.Domain._
import scala.util.continuations.suspendable
import com.typesafe.scalalogging.slf4j.StrictLogging

trait SimpleOperationController extends Observing with StrictLogging {

  val mainWindowView: MainWindowView
  val commandView: BaseCommandView
  val triggerEvent: Events[_]
  val continueEvent: Events[_]
  val abortEvent: Events[_]

  def reactorLoop(): Reactor = Reactor.loop {

    self =>
      self awaitNext triggerEvent
      logger.debug("Loop triggered")

      commandView.reset()
      mainWindowView.argumentsPanel() = Some(commandView.panel)

      self.abortOn(abortEvent) {
        self awaitNext continueEvent
        execute(self)
      }

      logger.debug("Loop done, resetting arguments panel")
      mainWindowView.argumentsPanel() = None
  }

  def execute(self: FlowOps): Unit@suspendable

}
