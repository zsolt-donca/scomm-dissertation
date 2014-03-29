package edu.zsd.testfw

import net.java.openjdk.cacio.ctc.{CTCGraphicsEnvironment, CTCToolkit}
import javax.swing.plaf.metal.MetalLookAndFeel
import org.junit.runners.BlockJUnit4ClassRunner

class CacioFESTLoggingRunner(clazz : Class[_]) extends BlockJUnit4ClassRunner(clazz) {
}

object CacioFESTLoggingRunner {
  System.setProperty("awt.toolkit", classOf[CTCToolkit].getName)
  System.setProperty("java.awt.graphicsenv", classOf[CTCGraphicsEnvironment].getName)
  System.setProperty("swing.defaultlaf", classOf[MetalLookAndFeel].getName)
  System.setProperty("java.awt.headless", "false")
}
