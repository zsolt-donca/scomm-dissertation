package edu.zsd.testfw

import org.aspectj.lang.JoinPoint
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object MethodCallStack {

  private[this] val executions: mutable.Stack[mutable.Buffer[Execution]] = mutable.Stack()

  private[this] var results: Seq[Execution] = Seq.empty

  def queryResults() : Seq[Execution] = {
    val results = this.results
    this.results = Seq.empty
    results
  }

  def enter(joinPoint: JoinPoint) {
    executions.push(new ArrayBuffer)
  }

  def exit(joinPoint: JoinPoint, result: AnyRef) {
    exit(joinPoint, ReturnResult(result))
  }

  def exit(joinPoint: JoinPoint, exception: Throwable) {
    exit(joinPoint, ExceptionResult(exception))
  }

  private[this] def exit(joinPoint: JoinPoint, result: Result) {
    val top: mutable.Seq[Execution] = executions.pop()
    val execution = Execution(joinPoint, top, result)

    if (executions.nonEmpty) {
      executions.top.append(execution)
    } else {
      results = results :+ execution
    }
  }
}

case class Execution(joinPoint: JoinPoint, invocations: Seq[Execution], result: Result)

sealed abstract class Result()

case class ReturnResult(result: AnyRef) extends Result

case class ExceptionResult(exception: Throwable) extends Result
