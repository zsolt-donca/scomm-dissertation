package edu.zsd.scomm

import scala.util.continuations.{suspendable, cpsParam}
import scala.collection.IterableLike
import java.nio.file.{DirectoryStream, Files, Path}
import scala.collection.JavaConverters._

object CPSUtils {

  object suspendable_block {
    def apply(comp: => Unit@suspendable): Unit@suspendable = {
      comp
    }
  }

  object unit {
    def apply() {
    }
  }

  implicit def cpsIterable[A, Repr](xs: IterableLike[A, Repr]) = new {
    def cps = new {
      def foreach[B](f: A => Any@cpsParam[Unit, Unit]): Unit@cpsParam[Unit, Unit] = {
        val it = xs.iterator
        while (it.hasNext) f(it.next())
      }
    }
  }

  def processRecursively(paths: Iterable[Path])(action: Path => Unit@suspendable): Unit@suspendable = {

    // instead of 'for (path: Path <- paths)'
    paths.cps foreach {
      path =>
        suspendable_block {
          if (Files.isDirectory(path)) {
            val directoryStream: DirectoryStream[Path] = Files.newDirectoryStream(path)
            try {
              processRecursively(directoryStream.asScala)(action)
              // instead of finally
              directoryStream.close()
            } catch {
              // instead of finally
              case e: Throwable => directoryStream.close(); throw e
            }
          }
        }
        action(path)
    }

  }

}
