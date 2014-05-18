package edu.zsd.scomm

import java.nio.file.{DirectoryStream, Files, Path}
import scala.collection.JavaConverters._

object FileUtils {

  def directoryList(path: Path): Seq[Path] = {
    val stream: DirectoryStream[Path] = Files.newDirectoryStream(path)
    try {
      val seq: Seq[Path] = stream.asScala.toIndexedSeq
      seq
    } finally {
      stream.close()
    }
  }

}
