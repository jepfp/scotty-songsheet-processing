package ch.scotty.converter.songanize

import ch.scotty.Db
import ch.scotty.converter.songanize.effect.{SongForUser, SongForUserListExportPathResolverAndCreator, SongForUserListFileCreator}
import ch.scotty.converter.songanize.load.AllowedSongIdsInAllGroupsForUserFinder
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SongForUserListExporter(songForUserListExportPathResolverAndCreator: SongForUserListExportPathResolverAndCreator = new SongForUserListExportPathResolverAndCreator())(implicit db: Db) {

  val songForUserListFileCreator: SongForUserListFileCreator = new SongForUserListFileCreator(songForUserListExportPathResolverAndCreator)

  val logger = Logger(classOf[SongForUserListExporter])

  def loadAndExport(userId: Long): Future[Seq[SongForUser]] = {
    val finder = new AllowedSongIdsInAllGroupsForUserFinder()
    for (
      songsForUser <- finder.find(userId);
      _ <- Future.fromTry(songForUserListFileCreator.createFile(userId, songsForUser))
    ) yield songsForUser
  }
}



