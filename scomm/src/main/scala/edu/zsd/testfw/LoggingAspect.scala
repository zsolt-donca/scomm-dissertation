package edu.zsd.testfw

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}
import org.fest.swing.image.{ImageFileExtensions, ScreenshotTaker}
import java.io.File
import edu.zsd.testfw.FESTLogging._
import java.lang.reflect.Method
import scala.compat.Platform
import edu.zsd.testfw.MethodCallStack.{RunningTestMethodExecution, RunningExecution}

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
    println("entering method: " + joinPoint)
    MethodCallStack.enterTestMethod(joinPoint)
    try {
      val result: AnyRef = joinPoint.proceed()
      println("exiting method: " + joinPoint)
      MethodCallStack.exitTestMethod(joinPoint, result)
      result
    } catch {
      case e: Throwable =>
        println("throwing from method: " + joinPoint)
        MethodCallStack.exitTestMethod(joinPoint, e)
        throw e
    }
  }

  @Around("testMethods() || beforeMethods() || afterMethods()")
  def takeScreenshotOfFailedTest(joinPoint: ProceedingJoinPoint) : AnyRef = {

    try {
      joinPoint.proceed()
    } catch {
      case e : Throwable =>
        val testMethodExecution: RunningTestMethodExecution = MethodCallStack.getCurrentRunningTestMethodExecution(joinPoint)
        if (testMethodExecution.afterScreenshot == None) {
          testMethodExecution.afterScreenshot = Some(takeScreenshot("failed"))
        }
        throw e
    }
  }

  private def takeScreenshot(kind : String) : File = {
    val filename: String = f"$currentTestIndex%04d_${Platform.currentTime}_$kind.${ImageFileExtensions.PNG}"
    val beforeScreenshot: File = new File(screenshotsDir, filename)
    new ScreenshotTaker().saveDesktopAsPng(beforeScreenshot.getPath)
    beforeScreenshot
  }
}
