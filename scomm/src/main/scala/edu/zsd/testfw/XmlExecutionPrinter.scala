package edu.zsd.testfw

import scala.xml.{Node, PrettyPrinter}
import java.io.{File, PrintWriter}

object XmlExecutionPrinter {
  def printToFile(filename : String, execution: Execution): Unit = {

    val xmlReportsDir: File = new File("./scala-2.10/classes/web/xml-reports")
    xmlReportsDir.mkdirs()

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

}
