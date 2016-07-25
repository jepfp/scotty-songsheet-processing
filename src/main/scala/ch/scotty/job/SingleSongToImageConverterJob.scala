package ch.scotty.job
import ch.scotty.job.json.{AllSongToImageConverterJobConfiguration, JobDefinitions, SingleSongToImageConverterJobConfiguration}

object SingleSongToImageConverterJob extends Job[SingleSongToImageConverterJobConfiguration]{
  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[SingleSongToImageConverterJobConfiguration]] = jobDefinitions.singleSongToImageConverterJob

  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Unit = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println("jobConfiguration.lied = " + jobConfiguration.songId)
  }
}
