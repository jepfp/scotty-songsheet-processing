package ch.scotty.job

import ch.scotty.job.json.{AllSongToImageConverterJobConfiguration, JobDefinitions, SingleSongToImageConverterJobConfiguration}

object AllSongToImageConverterJob extends Job[AllSongToImageConverterJobConfiguration]{
  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[AllSongToImageConverterJobConfiguration]] = jobDefinitions.allSongToImageConverterJob

  override def run(jobConfiguration: AllSongToImageConverterJobConfiguration): Unit = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println("Going to export all songs")
  }
}
