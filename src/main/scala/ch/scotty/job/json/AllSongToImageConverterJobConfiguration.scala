package ch.scotty.job.json

import java.util.UUID

case class AllSongToImageConverterJobConfiguration(override val jobId: UUID) extends JobConfiguration
