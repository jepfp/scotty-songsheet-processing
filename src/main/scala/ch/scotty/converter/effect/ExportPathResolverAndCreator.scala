package ch.scotty.converter.effect

import java.nio.file.{Files, Paths}

import ch.scotty.converter.SourceSystem
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

private[converter] class ExportPathResolverAndCreator(conf: Config) {

  val config = conf.getConfig("converter")
  val logger = Logger(classOf[ExportPathResolverAndCreator])

  def this() {
    this(ConfigFactory.load())
  }

  def resolve(sourceSystem : SourceSystem, filename: String): String = {
    val basePath = config.getString("exportBaseDir")
    Files.createDirectories(Paths.get(basePath, sourceSystem.getIdentifier))
    val path = Paths.get(basePath, sourceSystem.getIdentifier, filename).toAbsolutePath
    val pathString = path.toString
    logger.debug(s"Output path: $pathString")
    pathString
  }

}