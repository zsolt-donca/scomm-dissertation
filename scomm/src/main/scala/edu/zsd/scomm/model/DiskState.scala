package edu.zsd.scomm.model

import edu.zsd.scomm.Domain._
import org.springframework.stereotype.Component
import com.typesafe.scalalogging.slf4j.StrictLogging

@Component
class DiskState extends Observing with StrictLogging {

  private var counter: Long = 0

  private val _state = Var[Long](0)

  def apply(): Unit = _state()

  def refresh() {
    logger.debug("Refreshing disk state.")
    val next = counter + 1
    _state() = next
    counter = next
  }
}
