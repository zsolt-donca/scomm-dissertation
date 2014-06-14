package edu.zsd.cpsdemo

import scala.react.Domain
import java.util.concurrent.Executors

object MyDomain extends Domain {
  val scheduler = new ThreadPoolScheduler(Executors.newSingleThreadExecutor())
  val engine = new Engine
}

import MyDomain._


object ConsistencyExample extends App {

  class Example extends Observing {
    val firstName = Var[String]("")
    val lastName = Var[String]("")

    val fullName = Strict {
      firstName() + " " + lastName()
    }

    observe(fullName) { fullName => println(fullName)}
  }


  var example: Example = null

  start()
  schedule({
    example = new Example
  })
  schedule({
    example.firstName() = "John"
    example.lastName() = "Doe"
  })
  schedule({
    example.firstName() = "Billy"
    example.lastName() = "Bill"
  })

  case class Person(name: String, phone: String) {
    def normalizedPhone = "+40" + phone
  }

  val p1 = new Person("Joe", "7234")
  println(p1.normalizedPhone) // +407234
}
