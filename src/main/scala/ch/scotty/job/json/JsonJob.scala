package ch.scotty.job.json

import java.util.UUID

case class JsonJob[T](
  jobId: UUID,
  jobName: String,
  configuration: T)
