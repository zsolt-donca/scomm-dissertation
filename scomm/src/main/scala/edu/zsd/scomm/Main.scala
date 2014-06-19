
package edu.zsd.scomm

import java.nio.file.{Files, Paths}
import javax.swing.UIManager

import com.typesafe.scalalogging.slf4j.StrictLogging
import edu.zsd.scomm.Domain._
import edu.zsd.scomm.view.MainWindowView
import org.springframework.scala.context.function.FunctionalConfigApplicationContext

object Main extends ReactiveSimpleSwingApplication with StrictLogging {

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
      val path = Paths.get("C:\\")
      val dir = if (Files.exists(path)) path else Paths.get(".")
      AppParams(dir, dir)
    }

    applicationContext = FunctionalConfigApplicationContext[MainConfiguration]
    super.startup(args)
  }

  override def top = applicationContext.getBean(classOf[MainWindowView])

}



