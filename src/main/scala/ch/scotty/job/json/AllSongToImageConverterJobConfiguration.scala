package ch.scotty.job.json

import java.util.UUID

import ch.scotty.job.{AllSongToImageConverterJob, Job}

case class AllSongToImageConverterJobConfiguration(override val jobId: UUID) extends JobConfiguration
