package ch.scotty.converter

import ch.scotty.Db

class ConverterBySongId(implicit val db: Db) {

  protected lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected lazy val songnumberFinder = new SongnumberFinder()
  protected lazy val liedPdfToImageConverter = new LiedPdfToImageConverter()

  def convert(songId: Long) = {
    val liedData = liedSourcePdfFileFinder.findFile(songId)
    val songnumbers = songnumberFinder.findSongnumbers(songId)
    liedPdfToImageConverter.convertPdfBlobToImage(liedData, songnumbers)
  }

}