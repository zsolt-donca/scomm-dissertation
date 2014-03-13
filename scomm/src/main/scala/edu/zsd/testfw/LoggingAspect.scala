package edu.zsd.testfw

import org.aspectj.lang.annotation.{Before, Around, Pointcut, Aspect}
import org.aspectj.lang.ProceedingJoinPoint


@Aspect
class LoggingAspect {

  @Pointcut("execution(@org.junit.Test * *.*(..))")
  def testMethod() : Unit = {}

  @Pointcut("execution(* (@org.fest.swing.annotation.GUITest *).*(..))")
  def guiTestMethods() : Unit = {}

  @Around("testMethod() || guiTestMethods()")
  def aroundTestMethods(joinPoint : ProceedingJoinPoint) : AnyRef = {
    println("Invoking test method with signature: " + joinPoint.getSignature)
    try {
      joinPoint.proceed()
    } finally {
      println("Test method invocation done.")
    }
  }
}
