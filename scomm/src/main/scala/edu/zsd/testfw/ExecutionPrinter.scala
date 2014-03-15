package edu.zsd.testfw

import scala.xml.{Node, PrettyPrinter}

object ExecutionPrinter {
  def print(execution: Execution): Unit = {

    val printer = new PrettyPrinter(160, 2)
    println(printer.format(toXml(execution)))
  }

  def toXml(execution: Execution): Node = {

    val args = if (execution.joinPoint.getArgs != null) execution.joinPoint.getArgs.deep else Seq.empty

    <execution>
      <join-point>{execution.joinPoint}</join-point>
      {if (args.nonEmpty) {
      <args>
        {args.map(arg => <arg>{arg}</arg>)}
      </args>
    }}<result>
      {execution.result match {
        case ReturnResult(result) => <return-result>{result}</return-result>
        case ExceptionResult(exception) => <exception-result>{exception}</exception-result>
      }}
    </result>
    {if (execution.invocations.nonEmpty) {
      <invocations>
        {execution.invocations.map(invocation => toXml(invocation))}
      </invocations>
    }}
    </execution>
  }

}
