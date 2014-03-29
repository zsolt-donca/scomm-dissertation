package edu.zsd.testfw

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}

@Aspect
class LoggingAspect {

  @Pointcut("execution(@org.junit.Test * *.*(..))")
  def testMethods() {}

  @Pointcut("execution(@org.junit.Before * *.*(..))")
  def beforeMethods() {}

  @Pointcut("execution(@org.junit.After * *.*(..))")
  def afterMethods() {}

  @Pointcut("execution(* (@edu.zsd.testfw.GUITestBean *).*(..))")
  def guiTestBeanMethods() {}

  @Pointcut("execution(public * org.fest.swing.core.BasicRobot.*(..))")
  def robotMethods() {}

  @Around("testMethods() || beforeMethods() || afterMethods() || guiTestBeanMethods()")
  def loggingTestAction(joinPoint: ProceedingJoinPoint): AnyRef = {
    MethodCallStack.enter()
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
