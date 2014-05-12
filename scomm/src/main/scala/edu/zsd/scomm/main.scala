
package edu.zsd.scomm

import edu.zsd.scomm.domain._
import java.nio.file.Paths
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import edu.zsd.scomm.view.MainWindowView
import org.springframework.context.annotation.AnnotationConfigApplicationContext

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

  override def top = applicationContext.getBean(classOf[MainWindowView])

}



