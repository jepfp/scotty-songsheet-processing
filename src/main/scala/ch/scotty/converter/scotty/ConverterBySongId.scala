package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversion}
import ch.scotty.converter.LiedWithData

class ConverterBySongId(implicit val db: Db) {

  //VisibleForTesting
  protected[scotty] lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected[scotty] lazy val songnumberFinder = new SongnumberFinder()
  protected[scotty] lazy val liedPdfToImageConverter = new ConditionalLiedPdfToImageConverter()

  def convert(songId: Long): ConversionResult = {
    val liedData = liedSourcePdfFileFinder.findFile(songId)
    liedData match {
      case Right(data) => convertAndWritePicturesAndTableOfContents(songId, data)
      case Left(message) => FailedConversion(message)
    }
  }

  private def convertAndWritePicturesAndTableOfContents(songId: Long, data: LiedWithData) = {
    val songnumbers = songnumberFinder.findSongnumbers(songId)
    liedPdfToImageConverter.convertPdfBlobToImage(data, songnumbers)
  }
}