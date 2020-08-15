package ch.scotty.converter.songanize

import ch.scotty.converter.{FileType, LiedWithData, SourceSystem}

import scala.util.{Success, Try}


private class LiedWithDataBuilder {

  private final val baseSftpDir = "/private/files/"

  def build(song: SonganizeSong)(remoteFileLoader: RemoteFileLoader): Try[LiedWithData] = {
    remoteFileLoader.retrieveFile(baseSftpDir + song.pathOnFileSystem + "/" + song.filenameOnFileSystem)
      .flatMap(blob => {
        Success(LiedWithData(
          sourceSystem = SourceSystem.Songanize,
          songId = song.id,
          title = Option(song.title).filter(_.trim.nonEmpty).getOrElse(song.filename),
          tonality = Option(song.tonality).filter(_.trim.nonEmpty),
          tags = List.empty,
          createdAt = song.timestamp.toLocalDateTime,
          updatedAt = song.lastChange.toLocalDateTime,
          data = blob,
          fileType = FileType(song.fileType)
        ))
      })

  }

}
