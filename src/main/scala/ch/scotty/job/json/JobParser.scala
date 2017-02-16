package ch.scotty.job.json

import java.util.UUID

import ch.scotty.job.determinesongstoconvert.DetermineSongsToConvertJobConfiguration
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object JobParser {

  implicit val singleSongToImageConverterJobConfigurationReads: Reads[SingleSongToImageConverterJobConfiguration] = (
    (JsPath \ "jobId").read[UUID] and
      (JsPath \ "songId").read[Long]) (SingleSongToImageConverterJobConfiguration.apply _)

  implicit val allSongToImageConverterJobConfigurationReads: Reads[AllSongToImageConverterJobConfiguration] =
    (JsPath \ "jobId").read[UUID].map(AllSongToImageConverterJobConfiguration.apply _)

  implicit val determineSongsToConvertJobConfigurationReads: Reads[DetermineSongsToConvertJobConfiguration] =
    (JsPath \ "jobId").read[UUID].map(DetermineSongsToConvertJobConfiguration.apply _)

  implicit val jobDefinitionsReads: Reads[JobDefinitions] = (
    (JsPath \ "singleSongToImageConverterJob").readNullable[Seq[SingleSongToImageConverterJobConfiguration]] and
      (JsPath \ "allSongToImageConverterJob").readNullable[Seq[AllSongToImageConverterJobConfiguration]] and
      (JsPath \ "determineSongsToConvertJob").readNullable[Seq[DetermineSongsToConvertJobConfiguration]]) (JobDefinitions.apply _)

  def parseJobJson(json: String): JobDefinitions = {
    val parsedJson: JsValue = Json.parse(json)
    parsedJson.validate[JobDefinitions] match {
      case s: JsSuccess[JobDefinitions] =>
        val jobDefinitions: JobDefinitions = s.get
        if (jobDefinitions.allSongToImageConverterJob.isEmpty && jobDefinitions.singleSongToImageConverterJob.isEmpty && jobDefinitions.determineSongsToConvertJob.isEmpty) {
          throw new JobParsingException("No job definitions found.")
        }
        jobDefinitions
      case e: JsError =>
        throw new JobParsingException("Error while parsing jobs: " + JsError.toJson(e).toString())
    }

  }
}