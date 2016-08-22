package ch.scotty.job

import ch.scotty.Db
import ch.scotty.converter.{SongnumberFinder, _}
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}

class SingleSongToImageConverterJob(implicit val db: Db) extends Job[SingleSongToImageConverterJobConfiguration] {
  protected lazy val converterBySongId = new ConverterBySongId()


  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[SingleSongToImageConverterJobConfiguration]] = jobDefinitions.singleSongToImageConverterJob

  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Either[Failure, Success] = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println(s"Converting song with id ${jobConfiguration.songId}...")

    val songId = jobConfiguration.songId
    converterBySongId.convert(songId)
    Right(Success(jobConfiguration.jobId))
  }
}
