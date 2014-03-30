package edu.zsd.testfw

import net.java.openjdk.cacio.ctc.{CTCGraphicsEnvironment, CTCToolkit}
import javax.swing.plaf.metal.MetalLookAndFeel
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.{Statement, FrameworkMethod}

class CacioFESTLoggingRunner(clazz : Class[_]) extends BlockJUnit4ClassRunner(clazz) {

  System.setProperty("awt.toolkit", classOf[CTCToolkit].getName)
  System.setProperty("java.awt.graphicsenv", classOf[CTCGraphicsEnvironment].getName)
  System.setProperty("swing.defaultlaf", classOf[MetalLookAndFeel].getName)
  System.setProperty("java.awt.headless", "false")

  override def methodBlock(frameworkMethod: FrameworkMethod): Statement = {
    val statement: Statement = super.methodBlock(frameworkMethod)

    new Statement {
      override def evaluate(): Unit = {
        MethodCallStack.enterTest(frameworkMethod)
        var exception : Option[Throwable] = None
        try {
          statement.evaluate()
        } catch {
          case e: Throwable =>
            exception = Some(e)
            throw e
        } finally {
          val result = exception match {
            case Some(e) => ExceptionResult(e)
            case None => EmptyResult()
          }
          val execution = MethodCallStack.exitTest(frameworkMethod, result)
          XmlExecutionPrinter.printToFile(execution)
        }
      }
    }
  }
}

