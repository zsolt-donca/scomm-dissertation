
package edu.zsd.scomm

import edu.zsd.scomm.domain._
import java.nio.file.Paths
import edu.zsd.scomm.controller.MainWindow

object main extends ReactiveSimpleSwingApplication {

  var mainWindow : MainWindow = null
  override def startup(args: Array[String]): Unit = {
    val appParams = if (args.length >= 1) {
      val initDir = Paths.get(args(0))
      AppParams(initDir, initDir)
    } else {
      AppParams(Paths.get("C:\\"), Paths.get("D:\\"))
    }
    mainWindow = new MainWindow(appParams)
    super.startup(args)
  }

  override def top = mainWindow.frame
}



