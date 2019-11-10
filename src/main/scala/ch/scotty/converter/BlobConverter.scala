package ch.scotty.converter

import ch.scotty.converter.effect._

import scala.util.Try

trait BlobConverter {

  def exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()

  def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String): Try[Int]

  private[converter] def using[A <: {def close() : Unit}, B](param: A)(f: A => B): B =
    try f(param) finally param.close()

  private[converter] def generatePathString(sourceSystem: SourceSystem, songId: Long, i: Int, versionTimestamp: String) = {
    val filename = IdTimestampFilenameBuilder.build(songId, i, versionTimestamp)
    exportPathResolverAndCreator.resolve(sourceSystem, filename)
  }
}



