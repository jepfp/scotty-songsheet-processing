package ch.scotty.converter.songanize.effect

import better.files._
import com.typesafe.scalalogging.Logger

import scala.util.Try

private[songanize] class SongForUserListFileCreator(exportPathResolverAndCreator: SongForUserListExportPathResolverAndCreator) {

  import SongForUser.Formats._
  import play.api.libs.json._

  private val logger = Logger(classOf[SongForUserListFileCreator])

  def this() {
    this(new SongForUserListExportPathResolverAndCreator())
  }

  def createFile(userId : Long, songForUserList: Seq[SongForUser]): Try[Unit] = {
    Try{
      val resultString = Json.prettyPrint(Json.toJson(songForUserList))
      val file = File(exportPathResolverAndCreator.resolve(userId))
      file.overwrite(resultString)
    }
  }
}



