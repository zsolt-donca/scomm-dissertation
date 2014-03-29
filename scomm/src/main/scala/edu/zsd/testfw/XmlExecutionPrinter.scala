package edu.zsd.testfw

import scala.xml.{Node, PrettyPrinter}
import java.io.{File, PrintWriter}
import org.junit.runners.model.FrameworkMethod
import org.aspectj.lang.{Signature, JoinPoint}
import org.aspectj.lang.JoinPoint.StaticPart
import org.aspectj.lang.reflect.SourceLocation

object XmlExecutionPrinter {

  def printToFile(method: FrameworkMethod, executions: Seq[Execution]): Unit = {

    val xmlReportsDir: File = new File("./scala-2.10/classes/web/xml-reports")
    xmlReportsDir.mkdirs()

    val filename = s"report_${method.getName}.xml"
    val execution = Execution(createJoinPoint(method), executions, ReturnResult(null))

    val printWriter = new PrintWriter(new File(xmlReportsDir, filename))
    try {
      val prettyPrinter = new PrettyPrinter(160, 2)
      val xml: Node = toXml(execution)
      val xmlString: String = prettyPrinter.format(xml)
      printWriter.write(xmlString)
    } finally {
      printWriter.close()
    }
  }

  def toXml(execution: Execution): Node = {

    val args = if (execution.joinPoint.getArgs != null) execution.joinPoint.getArgs.deep else Seq.empty

    <execution>
      <join-point>{execution.joinPoint}</join-point>
      <args>{args}</args>
      {execution.result match {
        case ReturnResult(result) => <return-result>{result}</return-result>
        case ExceptionResult(exception) => <exception-result>{exception}</exception-result>
      }}
      {if (execution.invocations.nonEmpty) {
        <invocations>
          {execution.invocations.map(invocation => toXml(invocation))}
        </invocations>
      }}
    </execution>
  }

  def createJoinPoint(method: FrameworkMethod): JoinPoint = {

    new JoinPoint {
      override def getKind: String = ???

      override def getTarget: AnyRef = ???

      override def toLongString: String = ???

      override def getThis: AnyRef = ???

      override def getArgs: Array[AnyRef] = Array.empty

      override def getSourceLocation: SourceLocation = ???

      override def toShortString: String = ???

      override def getStaticPart: StaticPart = ???

      override def getSignature: Signature = ???

      override def toString: String = s"test ${method.getName}"
    }

  }

}
