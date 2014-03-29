package edu.zsd.testfw

import scala.xml.{Node, PrettyPrinter}
import java.io.{File, PrintWriter}
import java.lang.reflect.Method
import com.github.nscala_time.time.Imports._

object XmlExecutionPrinter {

  def printToFile(testExecution : Execution): Unit = {

    val reportsDir : File = new File("./reports")
    val xmlReportsDir: File = new File(reportsDir, "xml")
    xmlReportsDir.mkdirs()

    val method = testExecution.method

    val filename = s"report_${method.getName}.xml"
    val xmlFile: File = new File(xmlReportsDir, filename)

    val printWriter = new PrintWriter(xmlFile)
    try {
      val prettyPrinter = new PrettyPrinter(160, 2)
      val xml: Node = toXml(testExecution)
      val xmlString: String = prettyPrinter.format(xml)
      printWriter.write(xmlString)
    } finally {
      printWriter.close()
    }
  }

  private def toXml(execution: Execution): Node = {

    val args = execution.args.deep

    <execution>
      <method>{
        val method: Method = execution.method
        method.getDeclaringClass.getSimpleName + "." + method.getName
      }
      </method>
      <args>{args}</args>
      <result>
        {execution.result match {
          case EmptyResult() => <empty-result/>
          case ReturnResult(result) => <return-result>{result}</return-result>
          case ExceptionResult(exception) => <exception-result>{exception}</exception-result>
        }}
      </result>
      <start-time>{new DateTime(execution.startTime)}</start-time>
      <end-time>{new DateTime(execution.endTime)}</end-time>
      {if (execution.invocations.nonEmpty) {
        <invocations>
          {execution.invocations.map(invocation => toXml(invocation))}
        </invocations>
      }}
    </execution>
  }

}
