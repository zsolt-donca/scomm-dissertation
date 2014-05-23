
package edu.zsd.scomm

import edu.zsd.scomm.Domain._
import java.nio.file.Paths
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import edu.zsd.scomm.view.MainWindowView
import javax.swing.UIManager
import com.typesafe.scalalogging.slf4j.StrictLogging

object Main2 extends ReactiveSimpleSwingApplication with StrictLogging {

  var applicationContext: FunctionalConfigApplicationContext = null
  var appParams: AppParams = null

  override def startup(args: Array[String]): Unit = {

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    } catch {
      case e: Exception => logger.warn(s"Could not set system look and feel ${UIManager.getSystemLookAndFeelClassName}: $e", e)
    }

    appParams = if (args.length >= 1) {
      val initDir = Paths.get(args(0))
      AppParams(initDir, initDir)
    } else {
      AppParams(Paths.get("C:\\scomm-test"), Paths.get("C:\\scomm-test"))
    }

    applicationContext = FunctionalConfigApplicationContext[MainConfiguration]
    super.startup(args)
  }

  override def top = applicationContext.getBean(classOf[MainWindowView])

}



