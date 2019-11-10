package ch.scotty.converter

import better.files._
import ch.scotty.converter.effect._
import com.typesafe.scalalogging.Logger

import scala.util.Try

private class NoOpBlobConverter(override val exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) extends BlobConverter {

  val logger = Logger(classOf[NoOpBlobConverter])

  final val AmountOfPages = 1

  override def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String): Try[Int] = {
    Try {
      val file: File = File(generatePathString(sourceSystem, songId, 0, dataChecksum))
      logger.debug("Exporting with no conversion songId={}", songId)
      file.writeByteArray(data)
      AmountOfPages
    }
  }
}



