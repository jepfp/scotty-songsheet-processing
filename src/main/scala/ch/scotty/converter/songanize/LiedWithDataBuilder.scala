package ch.scotty.converter.songanize

import ch.scotty.converter.{FileType, LiedWithData, SourceSystem}

import scala.util.Try


private class LiedWithDataBuilder {

  def build(song: SonganizeSong, groupForUser: SongForUser)(remoteFileLoader: RemoteFileLoader): Try[LiedWithData] = {
    remoteFileLoader.retrieveFile(song.pathOnFileSystem + "/" + song.filenameOnFileSystem).map(blob => {
      LiedWithData(
        sourceSystem = SourceSystem.Songanize,
        songId = song.id,
        title = Option(song.title).filter(_.trim.nonEmpty).getOrElse(song.filename),
        tonality = Option(song.tonality).filter(_.trim.nonEmpty),
        tags = List(groupForUser.groupName), //the group (or "Private" for own songs) which shared this song with that user is added as a tag.
        createdAt = song.timestamp.toLocalDateTime,
        updatedAt = song.lastChange.toLocalDateTime,
        data = blob,
        fileType = FileType.fromString(song.fileType)
      )
    })

  }

}
