package edu.zsd.testfw

import org.aspectj.lang.{Signature, JoinPoint}
import scala.collection.mutable
import java.lang.reflect.Method
import org.aspectj.lang.reflect.MethodSignature
import scala.compat.Platform
import org.junit.runners.model.FrameworkMethod


object MethodCallStack {

  private[this] val executions: mutable.Stack[RunningExecution] = mutable.Stack()

  def enterTest() : Unit = {
    require(executions.isEmpty)
    executions.push(new RunningExecution)
  } ensuring executions.nonEmpty

  def enter() : Unit = {
    require(executions.nonEmpty)
    executions.push(new RunningExecution)
  } ensuring executions.nonEmpty

  def exit(joinPoint: JoinPoint, result: AnyRef) : Unit = {
    val option: Option[Execution] = exit(joinPoint, ReturnResult(result))
    assert(option == None)
  } ensuring executions.nonEmpty

  def exit(joinPoint: JoinPoint, exception: Throwable) : Unit = {
    val option: Option[Execution] = exit(joinPoint, ExceptionResult(exception))
    assert(option == None)
  } ensuring executions.nonEmpty

  def exitTest(frameworkMethod: FrameworkMethod, result : Result) : Execution = {
    val option: Option[Execution] = exit(frameworkMethod.getMethod, Array.empty, result)
    option.get
  } ensuring executions.isEmpty

  private[this] def exit(joinPoint: JoinPoint, result: Result) : Option[Execution] = {
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

  private[this] def exit(method : Method, args : Array[AnyRef], result: Result) : Option[Execution] = {

    require(executions.nonEmpty)
    val runningExecution: RunningExecution = executions.pop()
    val execution = Execution(method, args, runningExecution.startTime, Platform.currentTime, runningExecution.invocations, result)

    if (executions.nonEmpty) {
      executions.top.invocations :+= execution
      None
    } else {
      Some(execution)
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
