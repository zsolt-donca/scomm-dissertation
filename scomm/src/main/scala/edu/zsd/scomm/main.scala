
package edu.zsd.scomm

import edu.zsd.scomm.Domain._
import java.nio.file.Paths
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import edu.zsd.scomm.view.MainWindowView
import javax.swing.UIManager

object Main extends ReactiveSimpleSwingApplication {

  var applicationContext: FunctionalConfigApplicationContext = null
  var appParams: AppParams = null

  override def startup(args: Array[String]): Unit = {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

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



