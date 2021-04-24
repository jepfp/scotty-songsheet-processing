package ch.scotty.converter.songanize.effect

import ch.scotty.converter.{SongsheetConfig, SourceSystem}
import com.typesafe.scalalogging.Logger

import java.nio.file.{Files, Paths}

private[songanize] class SongForUserListExportPathResolverAndCreator(config: SongsheetConfig = SongsheetConfig.get()) {

  val logger = Logger(classOf[SongForUserListExportPathResolverAndCreator])

  def resolve(userId: Long): String = {
    val basePath = config.exportBaseDir
    Files.createDirectories(Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "songForUserList"))
    val path = Paths.get(basePath, SourceSystem.Songanize.getIdentifier, "songForUserList", userId.toString + ".json").toAbsolutePath.toString
    logger.debug(s"Output path: $path")
    path
  }

}