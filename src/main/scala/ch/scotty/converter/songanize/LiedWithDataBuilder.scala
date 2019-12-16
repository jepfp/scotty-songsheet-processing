package ch.scotty.converter.songanize

import ch.scotty.converter.songanize.effect.SongForUser
import ch.scotty.converter.{FileType, LiedWithData, SourceSystem}

import scala.util.{Failure, Success, Try}


private class LiedWithDataBuilder {

  private final val baseSftpDir = "/private/files/"

  def build(song: SonganizeSong, groupForUserList: Set[SongForUser])(remoteFileLoader: RemoteFileLoader): Try[LiedWithData] = {
    remoteFileLoader.retrieveFile(baseSftpDir + song.pathOnFileSystem + "/" + song.filenameOnFileSystem)
      .flatMap(blob => {
        groupForUserList.find(_.songId == song.id) match {
          case Some(groupForUser) =>
            Success(LiedWithData(
              sourceSystem = SourceSystem.Songanize,
              songId = song.id,
              title = Option(song.title).filter(_.trim.nonEmpty).getOrElse(song.filename),
              tonality = Option(song.tonality).filter(_.trim.nonEmpty),
              tags = List(groupForUser.groupName), //the group (or "Private" for own songs) which shared this song with that user is added as a tag.
              createdAt = song.timestamp.toLocalDateTime,
              updatedAt = song.lastChange.toLocalDateTime,
              data = blob,
              fileType = FileType(song.fileType)
            ))

          case None =>
            Failure(new IllegalStateException(s"Could not find songId=${song.id} in groupForUserList=$groupForUserList"))
        }
      })

  }

}
