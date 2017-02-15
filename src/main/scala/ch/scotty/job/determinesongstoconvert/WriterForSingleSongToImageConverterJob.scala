package ch.scotty.job.determinesongstoconvert

import java.io.File

import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}
import ch.scotty.util.FilePrinter
import play.api.libs.json.{Json, Writes}

class WriterForSingleSongToImageConverterJob {

  implicit private val singleSongToImageConverterJobConfigurationWrites = new Writes[SingleSongToImageConverterJobConfiguration] {
    def writes(singleSongToImageConverterJobConfiguration: SingleSongToImageConverterJobConfiguration) = Json.obj(
      "jobId" -> singleSongToImageConverterJobConfiguration.jobId,
      "songId" -> singleSongToImageConverterJobConfiguration.songId)
  }

//  implicit private val allSongToImageConverterJobConfigurationWrites = new Writes[AllSongToImageConverterJobConfiguration] {
//    def writes(allSongToImageConverterJobConfiguration: AllSongToImageConverterJobConfiguration) = Json.obj(
//      "jobId" -> allSongToImageConverterJobConfiguration.jobId)
//  }
//
//  implicit private val determineSongsToConvertJobConfigurationWrites = new Writes[DetermineSongsToConvertJobConfiguration] {
//    def writes(determineSongsToConvertJobConfiguration: DetermineSongsToConvertJobConfiguration) = Json.obj(
//      "jobId" -> determineSongsToConvertJobConfiguration.jobId)
//  }

  implicit private val jobDefinitionsWrites = new Writes[JobDefinitions] {
    def writes(jobDefinitions: JobDefinitions) = Json.obj(
      "singleSongToImageConverterJob" -> jobDefinitions.singleSongToImageConverterJob)
//      "allSongToImageConverterJob" -> jobDefinitions.allSongToImageConverterJob,
//      "determineSongsToConvertJob" -> jobDefinitions.determineSongsToConvertJob)
  }

  def generateAndWriteJobFile(resultFile: File, jobs: Seq[SingleSongToImageConverterJobConfiguration]): Unit = {
    val resultString = Json.prettyPrint(Json.toJson(JobDefinitions(Some(jobs), None, None)))
    FilePrinter.print(resultFile)(_.println(resultString))
  }
}
