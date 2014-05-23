package edu.zsd.scomm.operations

import java.nio.file.{FileAlreadyExistsException, Files, Path}
import java.io.IOException

class NewFolderOperation(val newFolderPath: Path) {

  def execute(): OperationResult = {
    try {
      Files.createDirectory(newFolderPath)
      Success()
    } catch {
      case e: FileAlreadyExistsException => FileAlreadyExists()
      case e: IOException => GenericError(e)
    }
  }

  abstract sealed class OperationResult()

  case class Success() extends OperationResult

  case class FileAlreadyExists() extends OperationResult

  case class GenericError(e: IOException) extends OperationResult

}

