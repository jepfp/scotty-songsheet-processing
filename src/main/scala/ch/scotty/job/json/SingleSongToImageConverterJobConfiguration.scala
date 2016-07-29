package ch.scotty.job.json

import java.util.UUID

case class SingleSongToImageConverterJobConfiguration(override val jobId: UUID, songId: Long) extends JobConfiguration
