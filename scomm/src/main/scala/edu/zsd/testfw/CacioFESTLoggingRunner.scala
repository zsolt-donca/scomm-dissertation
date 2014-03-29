package edu.zsd.testfw

import net.java.openjdk.cacio.ctc.{CTCGraphicsEnvironment, CTCToolkit}
import javax.swing.plaf.metal.MetalLookAndFeel
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.{Statement, FrameworkMethod}
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.ProceedingJoinPoint

class CacioFESTLoggingRunner(clazz : Class[_]) extends BlockJUnit4ClassRunner(clazz) {

  override def methodBlock(method: FrameworkMethod): Statement = {
    val statement: Statement = super.methodBlock(method)
    val methodName = method.getName

    new Statement {
      override def evaluate(): Unit = {
        println(s"Test method $methodName started!")
        try {
          statement.evaluate()
        } catch {
          case e: Throwable =>
            println("test failed..... taking screenshot or something")
            throw e
        } finally {
          val results: Seq[Execution] = MethodCallStack.queryResults()
          XmlExecutionPrinter.printToFile(method, results)
        }
      }
    }
  }
}

object CacioFESTLoggingRunner {
  System.setProperty("awt.toolkit", classOf[CTCToolkit].getName)
  System.setProperty("java.awt.graphicsenv", classOf[CTCGraphicsEnvironment].getName)
  System.setProperty("swing.defaultlaf", classOf[MetalLookAndFeel].getName)
  System.setProperty("java.awt.headless", "false")
}
