package ch.scotty.converter

import ch.scotty.Db
import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversion}

class ConverterBySongId(implicit val db: Db) {

  //VisibleForTesting
  protected[converter] lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected[converter] lazy val songnumberFinder = new SongnumberFinder()
  protected[converter] lazy val liedPdfToImageConverter = new LiedPdfToImageConverter()

  def convert(songId: Long): ConversionResult = {
    val liedData = liedSourcePdfFileFinder.findFile(songId)
    liedData match {
      case Right(data) => liedPdfToImageConverter.convertPdfBlobToImage(data, songnumberFinder.findSongnumbers(songId))
      case Left(message) => FailedConversion(message)
    }
  }

}