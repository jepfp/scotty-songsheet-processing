package ch.scotty.job.json

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

class SingleSongToImageConverterJobParser extends JobParser[SingleSongToImageConverterJobConfiguration] {
  implicit val configurationReads: Reads[SingleSongToImageConverterJobConfiguration] = (__ \ "songId").read[Long].map(SingleSongToImageConverterJobConfiguration.apply _)

  implicit val configurationWrites: Writes[SingleSongToImageConverterJobConfiguration] = new Writes[SingleSongToImageConverterJobConfiguration] {
    def writes(song: SingleSongToImageConverterJobConfiguration) = Json.obj(
      "songId" -> song.songId
    )
  }


}