package ch.scotty.converter.songanize

import ch.scotty.converter.ConverterExporter
import ch.scotty.converter.effect.{ExportPathResolverAndCreator, TableOfContentsDTOs}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Try}


class SonganizeConverter(exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) {
  val logger = Logger(classOf[SonganizeConverter])

  def convertSong(songInfo: SonganizeSong)(remoteFileLoader: RemoteFileLoader): Try[TableOfContentsDTOs.Song] = {
    val conversionResult: Try[TableOfContentsDTOs.Song] = for (
      liedData <- (new LiedWithDataBuilder()).build(songInfo)(remoteFileLoader);
      r <- (new ConverterExporter(exportPathResolverAndCreator)).convertAndExport(liedData, Seq.empty)
    ) yield r

    conversionResult recoverWith {
      case t: Throwable =>
        val m = s"Failure while converting songanize song. songInfo=$songInfo"
        Failure(new Exception(m, t))
    }
  }

}
