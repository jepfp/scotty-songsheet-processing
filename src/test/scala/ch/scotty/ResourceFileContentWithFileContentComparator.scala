package ch.scotty

import java.io.File

import org.scalatest.Assertions

import scala.io.BufferedSource

object ResourceFileContentWithFileContentComparator {
  def assertContentEquals(resourceClass: Class[_])(filenameOfResourceWithExpectedContent: String, fileWithActualContent: File) = {
    val expectedResultString = readResourceFile(resourceClass, filenameOfResourceWithExpectedContent)
    val actualResultString = sourceToString(scala.io.Source.fromFile(fileWithActualContent))
    Assertions.assertResult(expectedResultString)(actualResultString)
  }

  def readResourceFile(resourceClass: Class[_], filename: String): String = {
    val stream = resourceClass.getResourceAsStream(filename)
    sourceToString(scala.io.Source.fromInputStream(stream))
  }

  def sourceToString(source: BufferedSource): String = try source.mkString finally source.close()
}
