
package edu.zsd.scomm

import edu.zsd.scomm.domain._
import java.nio.file.Paths
import edu.zsd.scomm.controller.MainWindow
import org.springframework.scala.context.function.FunctionalConfigApplicationContext

object main extends ReactiveSimpleSwingApplication {

  var applicationContext : FunctionalConfigApplicationContext = null
  var appParams : AppParams = null
  override def startup(args: Array[String]): Unit = {
    appParams = if (args.length >= 1) {
      val initDir = Paths.get(args(0))
      AppParams(initDir, initDir)
    } else {
      AppParams(Paths.get("C:\\"), Paths.get("D:\\"))
    }

    applicationContext = FunctionalConfigApplicationContext[MainConfiguration]
    super.startup(args)
  }

  override def top = applicationContext.getBean(classOf[MainWindow]).mainWindowView

}



