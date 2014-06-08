package edu.zsd.festlogging

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}
import org.fest.swing.image.{ImageFileExtensions, ScreenshotTaker}
import java.io.File
import edu.zsd.festlogging.FESTLogging._
import scala.compat.Platform
import edu.zsd.festlogging.MethodCallStack.RunningTestMethodExecution
import scala.Some
import org.fest.swing.edt.{GuiQuery, GuiActionRunner}

@Aspect
class FESTLoggingAspect {

  @Pointcut("execution(@org.junit.Test * *.*(..))")
  def testMethods() {}

  @Pointcut("execution(@org.junit.Before * *.*(..))")
  def beforeMethods() {}

  @Pointcut("execution(@org.junit.After * *.*(..))")
  def afterMethods() {}

  @Pointcut("execution(* (@edu.zsd.festlogging.GUITestBean *).*(..))")
  def guiTestBeanMethods() {}

  @Pointcut("execution(@edu.zsd.festlogging.GUITestAction * *.*(..))")
  def guiTestActionMethods() {}

  @Pointcut("execution(@edu.zsd.festlogging.ExecuteInEDT * *.*(..))")
  def executeInEDTMethods() {}

  @Pointcut("execution(public * org.fest.swing.core.BasicRobot.*(..))")
  def robotMethods() {}

  @Around("executeInEDTMethods()")
  def executeInEDT(joinPoint: ProceedingJoinPoint) : AnyRef = {
    GuiActionRunner.execute(new GuiQuery[AnyRef] {
      override def executeInEDT(): AnyRef = joinPoint.proceed()
    })
  }

  @Around("testMethods() || beforeMethods() || afterMethods() || guiTestBeanMethods()")
  def reportTestAndGUITestBeanMethods(joinPoint: ProceedingJoinPoint): AnyRef = {
    MethodCallStack.enterTestMethod(joinPoint)
    try {
      val result: AnyRef = joinPoint.proceed()
      MethodCallStack.exitTestMethod(joinPoint, result)
      result
    } catch {
      case e: Throwable =>
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
        testMethodExecution.afterScreenshot = Some(takeScreenshot("failed"))
        throw e
    }
  }

  @Around("guiTestActionMethods()")
  def takeScreenshotsOfTestActions(joinPoint: ProceedingJoinPoint) : AnyRef = {
    val testMethodExecution: RunningTestMethodExecution = MethodCallStack.getCurrentRunningTestMethodExecution(joinPoint)
    testMethodExecution.beforeScreenshot = Some(takeScreenshot("before"))
    try {
      joinPoint.proceed()
    } finally {
      testMethodExecution.afterScreenshot = Some(takeScreenshot("after"))
    }
  }

  private def takeScreenshot(kind : String) : File = {
    val filename: String = f"$currentTestIndex%04d_${Platform.currentTime}_$kind.${ImageFileExtensions.PNG}"
    val beforeScreenshot: File = new File(screenshotsDir, filename)
    new ScreenshotTaker().saveDesktopAsPng(beforeScreenshot.getPath)
    beforeScreenshot
  }
}
