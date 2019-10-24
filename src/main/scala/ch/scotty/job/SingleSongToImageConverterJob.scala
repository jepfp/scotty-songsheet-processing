package ch.scotty.job

import ch.scotty.Db
import ch.scotty.converter._
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}

class SingleSongToImageConverterJob(implicit val db: Db) extends Job[SingleSongToImageConverterJobConfiguration] {
  protected lazy val converterBySongId = new ConverterBySongId()


  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[SingleSongToImageConverterJobConfiguration]] = jobDefinitions.singleSongToImageConverterJob

  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Either[Failure, Success] = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println(s"Converting song with id ${jobConfiguration.songId}...")

    val songId = jobConfiguration.songId
    val result = converterBySongId.convert(songId)
    mapResult(jobConfiguration, result)
  }

  private def mapResult(jobConfiguration: SingleSongToImageConverterJobConfiguration, result: ConversionResults.ConversionResult) = {
    result match {
      case ConversionResults.Success(_) => {
        Right(Success(jobConfiguration.jobId))
      }
      case ConversionResults.FailedConversion(message) => {
        Left(Failure(jobConfiguration.jobId, message, Seq()))
      }
      case ConversionResults.FailedConversionWithException(message, exception) => {
        Left(Failure(jobConfiguration.jobId, message, Seq(exception)))
      }
    }
  }
}
