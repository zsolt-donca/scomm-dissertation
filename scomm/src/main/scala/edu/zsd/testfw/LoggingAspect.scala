package edu.zsd.testfw

import org.aspectj.lang.annotation.{Before, Around, Pointcut, Aspect}
import org.aspectj.lang.ProceedingJoinPoint


@Aspect
class LoggingAspect {

  @Pointcut("execution(@org.junit.Test * *.*(..))")
  def testMethod() : Unit = {}

  @Pointcut("execution(* (@edu.zsd.testfw.GUITestBean *).*(..))")
  def guiTestBeanMethods() : Unit = {}

  @Around("testMethod()")
  def aroundTestMethods(joinPoint : ProceedingJoinPoint) : AnyRef = {
    println("Test started!")
    try {
      joinPoint.proceed()
    } catch {
      case e : Throwable =>
        println("test failed..... taking screenshot or something")
        throw e
    } finally {
      println("Test logged.")
    }
  }

  @Around("guiTestBeanMethods()")
  def loggingGuiTestMethod(joinPoint : ProceedingJoinPoint) {
    println("gui test bean method invoked with: " + joinPoint)
    joinPoint.proceed()
  }
}
