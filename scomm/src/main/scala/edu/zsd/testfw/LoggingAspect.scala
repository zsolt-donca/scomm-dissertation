package edu.zsd.testfw

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}
import scala.xml.PrettyPrinter

@Aspect
class LoggingAspect {

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
      val result: Option[Execution] = MethodStack.result
      println("Test logged. Result execution: " + result)

      result match {
        case Some(execution) =>
          ExecutionPrinter.print(execution)
      }
    }
  }

  @Around("testMethod() || guiTestBeanMethods()")
  def loggingTestAction(joinPoint: ProceedingJoinPoint): AnyRef = {
    MethodStack.enter(joinPoint)
    try {
      val result: AnyRef = joinPoint.proceed()
      MethodStack.exit(joinPoint, result)
      result
    } catch {
      case e: Throwable =>
        MethodStack.exit(joinPoint, e)
        throw e
    }
  }
}
