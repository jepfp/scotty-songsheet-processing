package ch.scotty.converter.blob

import better.files._
import ch.scotty.converter.effect._
import ch.scotty.converter.{FileType, SourceSystem}
import com.typesafe.scalalogging.Logger

import scala.util.Try

private[converter] class NoOpBlobConverter(override val exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) extends BlobConverter {

  val logger = Logger(classOf[NoOpBlobConverter])

  final val AmountOfPages = 1

  override def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String, inputFileType : FileType): Try[BlobConverterResult] = {
    Try {
      val file: File = File(generatePathString(sourceSystem, songId, 0, dataChecksum, inputFileType.concreteExtension))
      logger.debug("Exporting with no conversion songId={}", songId)
      file.writeByteArray(data)
      BlobConverterResult(AmountOfPages, inputFileType.concreteExtension)
    }
  }
}



