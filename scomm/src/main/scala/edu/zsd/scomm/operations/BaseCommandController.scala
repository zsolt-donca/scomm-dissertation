package edu.zsd.scomm.operations

import edu.zsd.scomm.view.MainWindowView
import edu.zsd.scomm.Domain._
import scala.util.continuations.suspendable
import com.typesafe.scalalogging.slf4j.StrictLogging

abstract class BaseCommandController(mainWindowView: MainWindowView) extends Observing with StrictLogging {

  val commandView: BaseCommandView
  val triggerEvent: Events[Any]
  val continueEvent: Events[Any]
  val abortEvent: Events[Any]

  def reactorLoop(): Reactor = Reactor.loop {

    self =>
      self awaitNext triggerEvent
      logger.debug("Loop triggered")

      commandView.reset()
      mainWindowView.argumentsPanel() = Some(commandView.panel)

      self.abortOn(abortEvent) {
        logger.debug("Executing main action")
        self awaitNext continueEvent
        execute()
      }

      logger.debug("Resetting arguments panel")
      mainWindowView.argumentsPanel() = None
  }

  def execute(): Unit@suspendable

}
