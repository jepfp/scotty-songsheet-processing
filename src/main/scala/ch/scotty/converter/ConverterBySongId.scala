package ch.scotty.converter

import ch.scotty.Db
import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversion, Success}

class ConverterBySongId(implicit val db: Db) {

  //VisibleForTesting
  protected[converter] lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected[converter] lazy val songnumberFinder = new SongnumberFinder()
  protected[converter] lazy val liedPdfToImageConverter = new LiedPdfToImageConverter()
  protected[converter] lazy val tableOfContentsFileCreator = new TableOfContentsFileCreator()

  def convert(songId: Long): ConversionResult = {
    val liedData = liedSourcePdfFileFinder.findFile(songId)
    liedData match {
      case Right(data) => convertAndWritePicturesAndTableOfContents(songId, data)
      case Left(message) => FailedConversion(message)
    }
  }

  private def convertAndWritePicturesAndTableOfContents(songId: Long, data: LiedWithData) = {
    val songnumbers = songnumberFinder.findSongnumbers(songId)
    val resultPictures: ConversionResult = liedPdfToImageConverter.convertPdfBlobToImage(data, songnumbers)
    val resultJson = tableOfContentsFileCreator.createFile(data, songnumbers)
    (resultPictures, resultJson) match {
      case (Success(), Success()) => Success()
      case (failurePictures, failureJson) => FailedConversion(s"Conversion of pictures or writing the json failed.\nresultPictures: $failurePictures\nresultJson: $failureJson")
    }
  }
}