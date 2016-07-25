package ch.scotty.job.json

import java.util.UUID

import ch.scotty.job.Job

trait JobConfiguration {
  val jobId: UUID
}
