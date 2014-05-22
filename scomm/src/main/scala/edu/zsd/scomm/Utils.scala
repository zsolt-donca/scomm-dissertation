package edu.zsd.scomm

import scala.util.continuations.{suspendable, cpsParam}
import scala.collection.IterableLike
import java.nio.file.{DirectoryStream, Files, Path}
import scala.collection.JavaConverters._

object Utils {

  object suspendable_block {
    def apply(comp: => Unit@suspendable): Unit@suspendable = {
      comp
    }
  }

  object unit {
    def apply() {
    }
  }

  def cps_foreach[A, Repr, B](xs: IterableLike[A, Repr])(f: A => Any@cpsParam[Unit, Unit]): Unit@cpsParam[Unit, Unit] = {
    val it = xs.iterator
    while (it.hasNext) f(it.next())
  }

  def walkPathsPreOrder(paths: Iterable[Path])(action: Path => Unit@suspendable): Unit@suspendable = {

    // instead of 'for (path: Path <- paths)'
    cps_foreach(paths) {
      path: Path =>
        action(path)
        suspendable_block {
          if (Files.isDirectory(path)) {
            val list: Seq[Path] = directoryList(path)
            walkPathsPreOrder(list)(action)
          }
        }
    }

  }

  def walkPathsPostOrder(paths: Iterable[Path])(action: Path => Unit@suspendable): Unit@suspendable = {

    // instead of 'for (path: Path <- paths)'
    cps_foreach(paths) {
      path: Path =>
        suspendable_block {
          if (Files.isDirectory(path)) {
            val list: Seq[Path] = directoryList(path)
            walkPathsPostOrder(list)(action)
          }
        }
        action(path)
    }
  }

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
