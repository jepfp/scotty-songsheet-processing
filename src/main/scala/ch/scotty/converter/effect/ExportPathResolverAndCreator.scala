package ch.scotty.converter.effect

import ch.scotty.converter.{SongsheetConfig, SourceSystem}
import com.typesafe.scalalogging.Logger

import java.nio.file.{Files, Paths}

private[converter] class ExportPathResolverAndCreator(config : SongsheetConfig = SongsheetConfig.get()) {

  val logger = Logger(classOf[ExportPathResolverAndCreator])

  def resolve(sourceSystem : SourceSystem, filename: String): String = {
    val basePath = config.exportBaseDir
    Files.createDirectories(Paths.get(basePath, sourceSystem.getIdentifier))
    val path = Paths.get(basePath, sourceSystem.getIdentifier, filename).toAbsolutePath
    val pathString = path.toString
    logger.debug(s"Output path: $pathString")
    pathString
  }

}