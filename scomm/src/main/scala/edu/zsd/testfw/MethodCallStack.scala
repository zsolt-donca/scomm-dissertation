package edu.zsd.testfw

import org.aspectj.lang.{Signature, JoinPoint}
import scala.collection.mutable
import java.lang.reflect.Method
import org.aspectj.lang.reflect.MethodSignature
import scala.compat.Platform
import org.junit.runners.model.FrameworkMethod


object MethodCallStack {

  private[this] val executions: mutable.Stack[RunningExecution] = mutable.Stack()

  private[this] var results : Seq[Execution] = Seq.empty

  def queryExecutions() : Seq[Execution] = {
    val results = this.results
    this.results = Seq.empty
    results
  }

  def enter() {
    executions.push(new RunningExecution)
  }

  def exit(joinPoint: JoinPoint, result: AnyRef) {
    exit(joinPoint, ReturnResult(result))
  }

  def exit(joinPoint: JoinPoint, exception: Throwable) {
    exit(joinPoint, ExceptionResult(exception))
  }

  def exit(frameworkMethod: FrameworkMethod, result : Result) {
    exit(frameworkMethod.getMethod, Array.empty, result)
  }

  private[this] def exit(joinPoint: JoinPoint, result: Result) {
    val signature: Signature = joinPoint.getSignature
    signature match {
      case methodSignature : MethodSignature =>
        val method = methodSignature.getMethod
        val args: Array[AnyRef] = if (joinPoint.getArgs != null) joinPoint.getArgs else Array.empty

        // this is a mess...
        val realResult = if (methodSignature.getReturnType == classOf[Unit] && result == ReturnResult(null)) EmptyResult() else result
        exit(method, args, realResult)
      // otherwise we don't know what to do
    }
  }

  private[this] def exit(method : Method, args : Array[AnyRef], result: Result) {

    val runningExecution: RunningExecution = executions.pop()
    val execution = Execution(method, args, runningExecution.startTime, Platform.currentTime, runningExecution.invocations, result)

    if (executions.nonEmpty) {
      executions.top.invocations :+= execution
    } else {
      results :+= execution
    }
  }

  private class RunningExecution {
    val startTime : Long = Platform.currentTime
    var invocations : Seq[Execution] = Seq.empty
  }
}

case class Execution(method : Method, args : Array[AnyRef], startTime : Long, endTime : Long, invocations: Seq[Execution], result: Result)

sealed abstract class Result()

case class EmptyResult() extends Result

case class ReturnResult(result: AnyRef) extends Result

case class ExceptionResult(exception: Throwable) extends Result
