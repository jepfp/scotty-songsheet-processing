package ch.scotty.converter

import java.time.LocalDateTime

case class LiedWithData(songId: Long, title: String, tonality : Option[String], createdAt : LocalDateTime, updatedAt : LocalDateTime, data: java.sql.Blob) {
}