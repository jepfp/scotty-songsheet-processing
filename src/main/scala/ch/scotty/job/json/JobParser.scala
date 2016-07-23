package ch.scotty.job.json

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object JobParser {

  implicit val singleSongToImageConverterJobConfigurationReads: Reads[SingleSongToImageConverterJobConfiguration] = (
    (JsPath \ "jobId").read[UUID] and
      (JsPath \ "songId").read[Long]) (SingleSongToImageConverterJobConfiguration.apply _)

  implicit val singleSongToImageConverterJobConfigurationWrites = new Writes[SingleSongToImageConverterJobConfiguration] {
    def writes(singleSongToImageConverterJobConfiguration: SingleSongToImageConverterJobConfiguration) = Json.obj(
      "jobId" -> singleSongToImageConverterJobConfiguration.jobId,
      "songId" -> singleSongToImageConverterJobConfiguration.songId)
  }

  implicit val allSongToImageConverterJobConfigurationReads: Reads[AllSongToImageConverterJobConfiguration] =
    (JsPath \ "jobId").read[UUID].map(AllSongToImageConverterJobConfiguration.apply _)

  implicit val allSongToImageConverterJobConfigurationWrites = new Writes[AllSongToImageConverterJobConfiguration] {
    def writes(allSongToImageConverterJobConfiguration: AllSongToImageConverterJobConfiguration) = Json.obj(
      "jobId" -> allSongToImageConverterJobConfiguration.jobId)
  }

  implicit val jobDefinitionsReads: Reads[JobDefinitions] = (
    (JsPath \ "singleSongToImageConverterJob").readNullable[Seq[SingleSongToImageConverterJobConfiguration]] and
      (JsPath \ "allSongToImageConverterJob").readNullable[Seq[AllSongToImageConverterJobConfiguration]]) (JobDefinitions.apply _)

  implicit val jobDefinitionsWrites = new Writes[JobDefinitions] {
    def writes(jobDefinitions: JobDefinitions) = Json.obj(
      "singleSongToImageConverterJob" -> jobDefinitions.singleSongToImageConverterJob,
      "allSongToImageConverterJob" -> jobDefinitions.allSongToImageConverterJob)
  }

  def parseJobJson(json: String): JobDefinitions = {
    val parsedJson: JsValue = Json.parse(json)
    parsedJson.validate[JobDefinitions] match {
      case s: JsSuccess[JobDefinitions] => {
        val jobDefinitions: JobDefinitions = s.get
        if (jobDefinitions.allSongToImageConverterJob.isEmpty && jobDefinitions.singleSongToImageConverterJob.isEmpty) {
          throw new JobParsingException("No job definitions found.")
        }
        jobDefinitions
      }
      case e: JsError => {
        throw new JobParsingException("Error while parsing jobs: " + JsError.toJson(e).toString())
      }
    }

  }
}