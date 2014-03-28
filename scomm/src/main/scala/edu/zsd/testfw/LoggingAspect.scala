package edu.zsd.testfw

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}
import scala.xml.PrettyPrinter

@Aspect
class LoggingAspect {

  var tests = 0

  @Pointcut("execution(@org.junit.Test * *.*(..))")
  def testMethod() {}

  @Pointcut("execution(* (@edu.zsd.testfw.GUITestBean *).*(..))")
  def guiTestBeanMethods() {}

  @Pointcut("execution(public * org.fest.swing.core.BasicRobot.*(..))")
  def robotMethods() {}

  @Around("testMethod()")
  def aroundTestMethods(joinPoint: ProceedingJoinPoint): AnyRef = {
    println("Test started!")
    try {
      joinPoint.proceed()
    } catch {
      case e: Throwable =>
        println("test failed..... taking screenshot or something")
        throw e
    } finally {
      val result: Option[Execution] = MethodCallStack.result
      println("Test logged. Result execution: " + result)
      
      result match {
        case Some(execution) =>
          val filename = f"report_$tests%04d.xml"
          tests += 1
          XmlExecutionPrinter.printToFile(filename, execution)
      }
    }
  }

  @Around("testMethod() || guiTestBeanMethods()")
  def loggingTestAction(joinPoint: ProceedingJoinPoint): AnyRef = {
    MethodCallStack.enter(joinPoint)
    try {
      val result: AnyRef = joinPoint.proceed()
      MethodCallStack.exit(joinPoint, result)
      result
    } catch {
      case e: Throwable =>
        MethodCallStack.exit(joinPoint, e)
        throw e
    }
  }
}
