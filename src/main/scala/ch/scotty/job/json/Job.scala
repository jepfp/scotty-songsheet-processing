package ch.scotty.job.json

import java.util.UUID

/**
  * Created by Philipp on 12.07.2016.
  */
case class Job(
  jobId: UUID,
  jobName: String,
  configuration: SingleSongToImageConverterJobConfiguration)
