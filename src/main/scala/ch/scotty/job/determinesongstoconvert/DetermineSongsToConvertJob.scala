package ch.scotty.job.determinesongstoconvert

import java.io.File

import ch.scotty.Db
import ch.scotty.job.Job
import ch.scotty.job.json.JobDefinitions
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.util.FilePrinter


class DetermineSongsToConvertJob(implicit val db: Db) extends Job[DetermineSongsToConvertJobConfiguration]{

  protected lazy val songIdsToConvertFinder = new SongIdsToConvertFinder()
  protected lazy val jobsPath: String = "generated_jobs.json"

  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[DetermineSongsToConvertJobConfiguration]] = jobDefinitions.determineSongsToConvertJob

  override def run(jobConfiguration: DetermineSongsToConvertJobConfiguration): Either[Failure, Success] = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    val songIdsToConvert = songIdsToConvertFinder.findSongIdsToConvert()
    FilePrinter.print(new File(jobsPath))(_.print(songIdsToConvert.mkString(", \n")))
    Right(Success(jobConfiguration.jobId))
  }
}
