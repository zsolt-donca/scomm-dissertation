package edu.zsd.scomm.model

import java.nio.file.Path

case class SelectionInfo(size: Long, files: Int, folders: Int, paths: Set[Path])

object SelectionInfo {

  object Nothing {
    def unapply(info: SelectionInfo): Option[_] =
      if (info.files == 0 && info.folders == 0) Some(Unit) else None
  }

  object SingleFile {
    def unapply(info: SelectionInfo): Option[Path] =
      if (info.files == 1 && info.folders == 0) Some(info.paths.toSeq(0)) else None
  }

  object MultipleFiles {
    def unapply(info: SelectionInfo): Option[(Int, Set[Path])] =
      if (info.files > 1 && info.folders == 0) Some((info.files, info.paths)) else None
  }

  object SingleFolder {
    def unapply(info: SelectionInfo): Option[Path] =
      if (info.files == 0 && info.folders == 1) Some(info.paths.toSeq(0)) else None
  }

  object MultipleFolders {
    def unapply(info: SelectionInfo): Option[(Int, Set[Path])] =
      if (info.files == 0 && info.folders > 1) Some((info.folders, info.paths)) else None
  }

  object FilesAndFolders {
    def unapply(info: SelectionInfo): Option[(Int, Int, Set[Path])] =
      if (info.files > 0 && info.folders > 0) Some((info.files, info.folders, info.paths)) else None
  }

}