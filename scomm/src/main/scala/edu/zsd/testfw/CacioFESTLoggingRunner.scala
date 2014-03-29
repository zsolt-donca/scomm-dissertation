package edu.zsd.testfw

import net.java.openjdk.cacio.ctc.{CTCGraphicsEnvironment, CTCToolkit}
import javax.swing.plaf.metal.MetalLookAndFeel
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.{Statement, FrameworkMethod}
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.ProceedingJoinPoint
import scala.compat.Platform

class CacioFESTLoggingRunner(clazz : Class[_]) extends BlockJUnit4ClassRunner(clazz) {

  override def methodBlock(frameworkMethod: FrameworkMethod): Statement = {
    val statement: Statement = super.methodBlock(frameworkMethod)
    val methodName = frameworkMethod.getName

    new Statement {
      override def evaluate(): Unit = {
        println(s"Test method $methodName started!")
        MethodCallStack.enter()
        var exception : Option[Throwable] = None
        try {
          statement.evaluate()
        } catch {
          case e: Throwable =>
            println("test failed..... taking screenshot or something")
            exception = Some(e)
            throw e
        } finally {
          val result = exception match {
            case Some(e) => ExceptionResult(e)
            case None => EmptyResult()
          }
          MethodCallStack.exit(frameworkMethod, result)
          val executions: Seq[Execution] = MethodCallStack.queryExecutions()
          for (execution <- executions) {
            XmlExecutionPrinter.printToFile(execution)
          }
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
