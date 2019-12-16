package ch.scotty.converter.songanize.effect

import java.nio.file.{Files, Paths}

import ch.scotty.converter.SourceSystem
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

private[songanize] class SongForUserListExportPathResolverAndCreator(conf: Config) {

  val config = conf.getConfig("converter")
  val logger = Logger(classOf[SongForUserListExportPathResolverAndCreator])

  def this() {
    this(ConfigFactory.load())
  }

  def resolve(userId: Long): String = {
    val basePath = config.getString("exportBaseDir")
    Files.createDirectories(Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "songForUserList"))
    val path = Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "songForUserList", userId.toString + ".json").toAbsolutePath.toString
    logger.debug(s"Output path: $path")
    path
  }

}