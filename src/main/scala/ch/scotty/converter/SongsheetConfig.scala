package ch.scotty.converter

import ch.scotty.converter.SongsheetConfig.{Pdf, Png}
import com.typesafe.config.{Config, ConfigFactory}

object SongsheetConfig {

  private def config(configOverride: Map[String, AnyRef] = Map.empty): Config = {
    import scala.jdk.CollectionConverters._
    ConfigFactory
      .parseMap(configOverride.asJava)  //to override stuff in tests
      .withFallback(ConfigFactory.load("application.local")) //to override stuff locally (application.local.conf), this file is excluded in gitignore
      .withFallback(ConfigFactory.load()) //the normal application.conf
      .getConfig("scotty-songsheet-processing")
  }

  def get(configOverride: Map[String, AnyRef] = Map.empty): SongsheetConfig = {
    val c: Config = config(configOverride)
    val pdfConfig = c.getConfig("pdf")
    val pngConfig = c.getConfig("png")
    val quantizeFilterConfig = pngConfig.getConfig("quantizeFilter")
    SongsheetConfig(
      c.getString("exportBaseDir"),
      c.getBoolean("persistUncompressedImage"),
      Pdf(pdfConfig.getInt("dpi")),
      Png(pngConfig.getInt("width"), QuantizeFilter(quantizeFilterConfig.getInt("colors"), quantizeFilterConfig.getBoolean("dither")))
    )
  }

  case class Pdf(dpi: Int)

  case class Png(width: Int, quantizeFilter: QuantizeFilter)

  case class QuantizeFilter(colors: Int, dither: Boolean)
}

case class SongsheetConfig(
                            exportBaseDir: String,
                            persistUncompressedImage : Boolean,
                            pdf: Pdf,
                            png: Png
                          )