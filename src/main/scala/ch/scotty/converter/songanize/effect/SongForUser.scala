package ch.scotty.converter.songanize.effect

import play.api.libs.json.{Json, OFormat}

object SongForUser {

  object Formats {
    implicit val songForUserFormat: OFormat[SongForUser] = Json.format[SongForUser]
  }

}

case class SongForUser(groupName: String, songId: Long)
