package ch.scotty.converter

import java.time.LocalDateTime

object TableOfContentsDTOs {

  case class Song(songId: Long,
                  title: String,
                  tonality: Option[String],
                  songnumbers: Seq[Songnumber],
                  amountOfPages: Int,
                  //used to check, if pdf shall be overwritten by scotty-songsheet-processing, because it changed in scotty
                  pdfSourceChecksum: String,
                  //timestamp when data on songship was changed last. Can be used by a consumer to decide if image data has to be refetched or not
                  versionTimestamp : String,
                  createdAtInScotty: LocalDateTime,
                  updatedAtInScotty: LocalDateTime) {}

  case class Songnumber(songbookId: Long,
                        mnemonic: String,
                        songbookTitle: String,
                        number: String) {

  }

}
