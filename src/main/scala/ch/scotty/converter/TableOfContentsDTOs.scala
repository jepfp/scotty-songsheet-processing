package ch.scotty.converter

import java.time.LocalDateTime

object TableOfContentsDTOs {

  case class Song(songId: Long,
                  title: String,
                  tonality: Option[String],
                  songnumbers: Seq[Songnumber],
                  amountOfPages: Int,
                  pdfSourceVersion : String,
                  createdAt: LocalDateTime,
                  updatedAt: LocalDateTime) {}

  case class Songnumber(songbookId: Long,
                        mnemonic: String,
                        songbookTitle: String,
                        number: String) {

  }

}
