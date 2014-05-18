package edu.zsd.scomm.model

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import edu.zsd.scomm.Domain._

@Component
class MainWindowModel @Autowired()(val directoriesPaneModel: DirectoriesPaneModel) extends Observing {

  val status = Var[String]("")

  status() = "Ready"
}
