package ch.scotty.job.json

import java.util.UUID

import play.api.libs.json.Json.JsValueWrapper

case class SingleSongToImageConverterJobConfiguration(override val jobId: UUID, songId: Long) extends JobConfiguration
