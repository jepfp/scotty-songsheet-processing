package ch.scotty.job

import java.util.UUID

case class Job(
  jobId: UUID,
  liedId: Long,
  output: String)

  