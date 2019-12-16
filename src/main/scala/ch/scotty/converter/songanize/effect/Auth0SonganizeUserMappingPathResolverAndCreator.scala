package ch.scotty.converter.songanize.effect

import java.nio.file.{Files, Paths}

import ch.scotty.converter.SourceSystem
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

private[effect] class Auth0SonganizeUserMappingPathResolverAndCreator(conf: Config) {

  val config = conf.getConfig("converter")
  val logger = Logger(classOf[Auth0SonganizeUserMappingPathResolverAndCreator])

  def this() {
    this(ConfigFactory.load())
  }

  def resolve(): String = {
    val basePath = config.getString("exportBaseDir")
    Files.createDirectories(Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "auth0SonganizeUserMapping"))
    val path = Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "auth0SonganizeUserMapping", "mapping.json").toAbsolutePath.toString
    logger.debug(s"Output path: $path")
    path
  }

}