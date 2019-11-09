package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.converter.effect.TableOfContentsDTOs
import ch.scotty.converter.{ConverterExporter, LiedWithData}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Try}

class ConverterBySongId(implicit val db: Db) {

  //VisibleForTesting
  protected[scotty] lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected[scotty] lazy val songnumberFinder = new SongnumberFinder()
  protected[scotty] lazy val converterExporter = new ConverterExporter()

  val logger = Logger(classOf[ConverterBySongId])

  def convert(songId: Long): Try[TableOfContentsDTOs.Song] = {
    Try {
      val conversionResult: Try[TableOfContentsDTOs.Song] = for (
        liedData <- liedSourcePdfFileFinder.findFile(songId);
        r <- convertAndWritePicturesAndTableOfContents(songId, liedData)
      ) yield r

      conversionResult recoverWith {
        case t: Throwable =>
          val m = s"Failure while converting song with id $songId."
          logger.warn(m, t)
          Failure(new Exception(m, t))
      }
    }.flatten
  }

  private def convertAndWritePicturesAndTableOfContents(songId: Long, data: LiedWithData) = {
    val songnumbers = songnumberFinder.findSongnumbers(songId)
    converterExporter.convertAndExport(data, songnumbers)
  }
}