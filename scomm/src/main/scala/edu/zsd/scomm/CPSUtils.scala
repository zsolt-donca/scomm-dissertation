package edu.zsd.scomm

import scala.util.continuations.{suspendable, cpsParam}
import scala.collection.IterableLike
import java.nio.file.{Files, Path}

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

  def walkPathsPreOrder(paths: Iterable[Path])(action: Path => Unit@suspendable): Unit@suspendable = {

    // instead of 'for (path: Path <- paths)'
    paths.cps foreach {
      path =>
        action(path)
        suspendable_block {
          if (Files.isDirectory(path)) {
            val list: Seq[Path] = FileUtils.directoryList(path)
            walkPathsPreOrder(list)(action)
          }
        }
    }

  }

  def walkPathsPostOrder(paths: Iterable[Path])(action: Path => Unit@suspendable): Unit@suspendable = {

    // instead of 'for (path: Path <- paths)'
    paths.cps foreach {
      path =>
        suspendable_block {
          if (Files.isDirectory(path)) {
            val list: Seq[Path] = FileUtils.directoryList(path)
            walkPathsPostOrder(list)(action)
          }
        }
        action(path)
    }
  }

}
