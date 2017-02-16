package ch.scotty.job

import ch.scotty.Db
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.job.json.{AllSongToImageConverterJobConfiguration, JobDefinitions}

class AllSongToImageConverterJob(implicit val db: Db) extends Job[AllSongToImageConverterJobConfiguration]{
  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[AllSongToImageConverterJobConfiguration]] = jobDefinitions.allSongToImageConverterJob

  override def run(jobConfiguration: AllSongToImageConverterJobConfiguration): Either[Failure, Success] = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    throw new UnsupportedOperationException("AllSongToImageConverterJob is not yet implemented")
  }
}
