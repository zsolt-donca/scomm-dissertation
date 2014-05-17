package edu.zsd.scomm.model

import edu.zsd.scomm.domain._
import org.springframework.stereotype.Component

@Component
class DiskState extends Observing {

  private var counter: Long = 0

  private val _state = Var[Long](0)

  def apply(): Unit = _state()

  def refresh() {
    val next = counter + 1
    _state() = next
    counter = next
  }
}
