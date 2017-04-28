package ch.scotty.converter

import ch.scotty.Db

class ConverterBySongId(implicit val db: Db) {

  //VisibleForTesting
  protected[converter] lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected[converter] lazy val songnumberFinder = new SongnumberFinder()
  protected[converter] lazy val liedPdfToImageConverter = new LiedPdfToImageConverter()

  def convert(songId: Long) = {
    val liedData = liedSourcePdfFileFinder.findFile(songId)
    val songnumbers = songnumberFinder.findSongnumbers(songId)
    liedPdfToImageConverter.convertPdfBlobToImage(liedData, songnumbers)
    Unit
  }

}