package ch.scotty.job.determinesongstoconvert

import java.io.File
import java.util.UUID

import ch.scotty.Db
import ch.scotty.job.Job
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}


class DetermineSongsToConvertJob(implicit val db: Db) extends Job[DetermineSongsToConvertJobConfiguration]{

  protected lazy val songIdsToConvertFinder = new SongIdsToConvertFinder()
  protected lazy val writerForSingleSongToImageConverterJob = new WriterForSingleSongToImageConverterJob()
  protected lazy val jobsPath: String = "generated_jobs.json"

  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[DetermineSongsToConvertJobConfiguration]] = jobDefinitions.determineSongsToConvertJob

  override def run(jobConfiguration: DetermineSongsToConvertJobConfiguration): Either[Failure, Success] = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    val songIdsToConvert = songIdsToConvertFinder.findSongIdsToConvert()
    writerForSingleSongToImageConverterJob.generateAndWriteJobFile(new File(jobsPath), songIdsToConvert.map(SingleSongToImageConverterJobConfiguration(UUID.randomUUID(), _)))
    Right(Success(jobConfiguration.jobId))
  }
}
