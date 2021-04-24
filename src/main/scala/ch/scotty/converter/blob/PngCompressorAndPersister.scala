package ch.scotty.converter.blob

import ch.scotty.converter.SongsheetConfig
import com.sksamuel.scrimage.AwtImage
import com.sksamuel.scrimage.filter.QuantizeFilter
import com.sksamuel.scrimage.nio.PngWriter

import java.awt.image.BufferedImage
import java.io.File

private[converter] class PngCompressorAndPersister(config: SongsheetConfig = SongsheetConfig.get()) {
  def compressAndPersist(bim: BufferedImage, pathOut: String): Unit = {
    val pngConfig = config.png
    val image = new AwtImage(bim).toImmutableImage
    val scaledImage = image.scaleToWidth(config.png.width).filter(new QuantizeFilter(pngConfig.quantizeFilter.colors, pngConfig.quantizeFilter.dither))
    scaledImage.output(new PngWriter(), new File(pathOut))
  }
}



