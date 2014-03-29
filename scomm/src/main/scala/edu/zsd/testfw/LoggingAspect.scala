package edu.zsd.testfw

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}
import scala.xml.PrettyPrinter

@Aspect
class LoggingAspect {

  @Pointcut("execution(@org.junit.Test * *.*(..))")
  def testMethod() {}

  @Pointcut("execution(@org.junit.Before * *.*(..))")
  def beforeMethod() {}

  @Pointcut("execution(@org.junit.After * *.*(..))")
  def afterMethod() {}

  @Pointcut("execution(* (@edu.zsd.testfw.GUITestBean *).*(..))")
  def guiTestBeanMethods() {}

  @Pointcut("execution(public * org.fest.swing.core.BasicRobot.*(..))")
  def robotMethods() {}

  @Around("testMethod() || beforeMethod() || afterMethod() || guiTestBeanMethods()")
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
