package edu.zsd.festlogging

import scala.xml.{Node, PrettyPrinter}
import java.io.{File, PrintWriter}
import java.lang.reflect.Method
import com.github.nscala_time.time.Imports._
import edu.zsd.festlogging.FESTLogging._

object XmlExecutionPrinter {


  def printToFile(testExecution: Execution): Unit = {

    val method = testExecution.method
    val filename = f"report_$currentTestIndex%04d_${method.getDeclaringClass.getSimpleName}.${method.getName}.xml"
    val xmlFile: File = new File(xmlDir, filename)

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
      <method>
        {val method: Method = execution.method
      method.getDeclaringClass.getSimpleName + "." + method.getName}
      </method>
      <args>
        {args}
      </args>
      <result>
        {execution.result match {
        case EmptyResult() => <empty-result/>
        case ReturnResult(result) => <return-result>
          {result}
        </return-result>
        case ExceptionResult(exception) => <exception-result>
          {val exStr = exception.toString
          if (exStr.length > 90) exStr.substring(0, 90) + "..." else exStr}
        </exception-result>
      }}
      </result>
      <start-time>
        {new DateTime(execution.startTime)}
      </start-time>
      <end-time>
        {new DateTime(execution.endTime)}
      </end-time>{if (execution.invocations.nonEmpty) {
      <invocations>
        {execution.invocations.map(invocation => toXml(invocation))}
      </invocations>
    }}{execution.screenshot match {
      case Some(file) => <screenshot>
        {file.getCanonicalFile.toURI}
      </screenshot>
      case None =>
    }}
    </execution>
  }

}
