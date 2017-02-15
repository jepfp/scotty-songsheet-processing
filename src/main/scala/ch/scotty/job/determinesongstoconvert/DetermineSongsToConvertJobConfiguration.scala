package ch.scotty.job.determinesongstoconvert

import java.util.UUID

import ch.scotty.job.json.JobConfiguration

case class DetermineSongsToConvertJobConfiguration(override val jobId: UUID) extends JobConfiguration
