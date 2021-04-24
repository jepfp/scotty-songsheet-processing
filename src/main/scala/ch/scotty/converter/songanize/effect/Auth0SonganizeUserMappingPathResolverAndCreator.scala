package ch.scotty.converter.songanize.effect

import ch.scotty.converter.{SongsheetConfig, SourceSystem}
import com.typesafe.scalalogging.Logger

import java.nio.file.{Files, Paths}

private[effect] class Auth0SonganizeUserMappingPathResolverAndCreator(config: SongsheetConfig = SongsheetConfig.get()) {

  val logger = Logger(classOf[Auth0SonganizeUserMappingPathResolverAndCreator])

  def resolve(): String = {
    val basePath = config.exportBaseDir
    Files.createDirectories(Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "auth0SonganizeUserMapping"))
    val path = Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "auth0SonganizeUserMapping", "mapping.json").toAbsolutePath.toString
    logger.debug(s"Output path: $path")
    path
  }

}