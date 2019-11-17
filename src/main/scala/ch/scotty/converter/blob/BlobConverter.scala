package ch.scotty.converter.blob

import ch.scotty.converter.effect.{ExportPathResolverAndCreator, IdTimestampFilenameBuilder}
import ch.scotty.converter.{FileType, SourceSystem}

import scala.util.Try

private[converter] trait BlobConverter {

  def exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()

  def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String, inputFileType : FileType): Try[BlobConverterResult]

  private[converter] def using[A <: {def close() : Unit}, B](param: A)(f: A => B): B =
    try f(param) finally param.close()

  private[converter] def generatePathString(sourceSystem: SourceSystem, songId: Long, i: Int, versionTimestamp: String, outputFileType : String) = {
    val filename = IdTimestampFilenameBuilder.build(songId, i, versionTimestamp, outputFileType)
    exportPathResolverAndCreator.resolve(sourceSystem, filename)
  }
}
