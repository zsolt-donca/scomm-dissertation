package edu.zsd.festlogging

import org.aspectj.lang.{Signature, JoinPoint}
import java.lang.reflect.Method
import org.aspectj.lang.reflect.MethodSignature
import scala.compat.Platform
import org.junit.runners.model.FrameworkMethod
import java.io.File
import scala.annotation.tailrec


object MethodCallStack {

  private[this] var current: Option[RunningExecution] = None

  def enterTest(frameworkMethod: FrameworkMethod): Unit = {
    FESTLogging.beginTest()

    require(current == None)
    current = Some(new RunningTestExecution(frameworkMethod))
  } ensuring (current != None)

  def enterTestMethod(joinPoint: JoinPoint): Unit = {
    current match {
      case Some(currentRunningExecution) =>
        current = Some(new RunningTestMethodExecution(joinPoint, currentRunningExecution))
      case None => // ignore
    }
  }

  def exitTestMethod(joinPoint: JoinPoint, result: AnyRef): Unit = {
    val res = if (obtainReturnType(joinPoint) != classOf[Unit]) ReturnResult(result) else EmptyResult()
    exitTestMethod(joinPoint, res)
  }

  def exitTestMethod(joinPoint: JoinPoint, exception: Throwable): Unit = {
    exitTestMethod(joinPoint, ExceptionResult(exception))
  }

  private[this] def exitTestMethod(joinPoint: JoinPoint, result: Result): Unit = {
    current match {
      case Some(currentRunningExecution) =>
        currentRunningExecution match {
          case runningTestMethodExecution: RunningTestMethodExecution =>
            assert(runningTestMethodExecution.joinPoint == joinPoint)
            val args: Array[AnyRef] = if (joinPoint.getArgs != null) joinPoint.getArgs else Array.empty
            val currentExecution = createExecution(runningTestMethodExecution, args, result)
            val parentRunningExecution: RunningExecution = runningTestMethodExecution.parentRunningExecution
            parentRunningExecution.invocations :+= currentExecution
            current = Some(parentRunningExecution)
        }
      case None => // ignore
    }
  }

  def exitTest(frameworkMethod: FrameworkMethod, result: Result): Execution = {
    val currentRunningExecution = current.get
    assert(currentRunningExecution.method == frameworkMethod.getMethod)
    val currentExecution = createExecution(currentRunningExecution, Array.empty, result)
    currentRunningExecution match {
      case _: RunningTestExecution =>
        current = None
        currentExecution
    }
  }

  def getCurrentRunningTestMethodExecution(joinPoint: JoinPoint): RunningTestMethodExecution = {
    val currentRunningExecution = current.get
    currentRunningExecution match {
      case runningTestMethodExecution: RunningTestMethodExecution =>
        runningTestMethodExecution
    }
  }

  def getCurrentRunningTestExecution(joinPoint: JoinPoint): RunningTestExecution = {
    val currentRunningExecution = current.get
    getCurrentRunningTestExecution(currentRunningExecution)
  }

  @tailrec
  private def getCurrentRunningTestExecution(runningExecution: RunningExecution): RunningTestExecution = {
    runningExecution match {
      case runningTestExecution: RunningTestExecution =>
        runningTestExecution
      case runningTestMethodExecution: RunningTestMethodExecution =>
        getCurrentRunningTestExecution(runningTestMethodExecution.parentRunningExecution)
    }
  }

  private def obtainMethod(joinPoint: JoinPoint): Method = {
    val signature: Signature = joinPoint.getSignature
    signature match {
      case methodSignature: MethodSignature =>
        methodSignature.getMethod
    }
  }

  private def obtainReturnType(joinPoint: JoinPoint): Class[_] = {
    val signature: Signature = joinPoint.getSignature
    signature match {
      case methodSignature: MethodSignature =>
        methodSignature.getReturnType
    }
  }

  private def createExecution(runningExecution: RunningExecution, args: Array[AnyRef], result: Result): Execution = {
    Execution(runningExecution.method, args, runningExecution.startTime, Platform.currentTime,
      runningExecution.invocations, result,
      runningExecution.beforeScreenshot, runningExecution.afterScreenshot)
  }

  abstract sealed class RunningExecution(val method: Method) {
    val depth: Int

    val startTime: Long = Platform.currentTime
    var invocations: Seq[Execution] = Seq.empty

    var beforeScreenshot: Option[File] = None
    var afterScreenshot: Option[File] = None
  }

  class RunningTestExecution(val frameworkMethod: FrameworkMethod) extends RunningExecution(frameworkMethod.getMethod) {
    val depth = 0
  }

  class RunningTestMethodExecution(val joinPoint: JoinPoint, val parentRunningExecution: RunningExecution) extends RunningExecution(obtainMethod(joinPoint)) {
    val depth = parentRunningExecution.depth + 1
  }

}

case class Execution(method: Method,
                     args: Array[AnyRef],
                     startTime: Long,
                     endTime: Long,
                     invocations: Seq[Execution],
                     result: Result,
                     beforeScreenshot: Option[File],
                     afterScreenshot: Option[File])

sealed abstract class Result()

case class EmptyResult() extends Result

case class ReturnResult(result: AnyRef) extends Result

case class ExceptionResult(exception: Throwable) extends Result
