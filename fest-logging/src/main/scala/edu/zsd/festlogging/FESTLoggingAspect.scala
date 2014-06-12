package edu.zsd.festlogging

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect, Pointcut}
import org.fest.swing.image.{ImageFileExtensions, ScreenshotTaker}
import java.io.File
import edu.zsd.festlogging.FESTLogging._
import scala.compat.Platform
import edu.zsd.festlogging.MethodCallStack.{RunningExecution, RunningTestMethodExecution}
import scala.Some
import org.fest.swing.edt.{GuiQuery, GuiActionRunner}
import javax.swing.SwingWorker

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

  @Around("executeInEDTMethods()")
  def executeInEDT(joinPoint: ProceedingJoinPoint): AnyRef = {
    GuiActionRunner.execute(new GuiQuery[AnyRef] {
      override def executeInEDT(): AnyRef = joinPoint.proceed()
    })
  }

  @Around("testMethods() || beforeMethods() || afterMethods() || guiTestBeanMethods()")
  def auditMethods(joinPoint: ProceedingJoinPoint): AnyRef = {
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
  def takeScreenshotOfFailedTest(joinPoint: ProceedingJoinPoint): AnyRef = {
    try {
      joinPoint.proceed()
    } catch {
      case e: Throwable =>
        val testMethodExecution: RunningTestMethodExecution = MethodCallStack.getCurrentRunningTestMethodExecution(joinPoint)
        takeScreenshot("failed", testMethodExecution)
        throw e
    }
  }

  @Around("guiTestActionMethods()")
  def takeScreenshotsOfTestActions(joinPoint: ProceedingJoinPoint): AnyRef = {
    val testMethodExecution: RunningTestMethodExecution = MethodCallStack.getCurrentRunningTestMethodExecution(joinPoint)
    try {
      joinPoint.proceed()
    } finally {
      takeScreenshot("after", testMethodExecution)
    }
  }

  private val screenshotTaker: ScreenshotTaker = new ScreenshotTaker()

  private def takeScreenshot(kind: String, runningExecution: RunningExecution) {
    val filename: String = f"$currentTestIndex%04d_${Platform.currentTime}_$kind.${ImageFileExtensions.PNG}"
    val screenshot = screenshotTaker.takeDesktopScreenshot()
    val screenshotFile: File = new File(screenshotsDir, filename)
    val worker = new SwingWorker[Unit, Unit] {
      override def doInBackground(): Unit = {
        screenshotTaker.saveImage(screenshot, screenshotFile.toString)
      }
    }
    worker.execute()
    runningExecution.screenshot = Some(screenshotFile)
  }
}
