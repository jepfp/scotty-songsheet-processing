package ch.scotty.job.json

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

class JobParser {
  implicit val configurationReads: Reads[SingleSongToImageConverterJobConfiguration] = (__ \ "songId").read[Long].map(SingleSongToImageConverterJobConfiguration.apply _)

  implicit val configurationWrites: Writes[SingleSongToImageConverterJobConfiguration] = new Writes[SingleSongToImageConverterJobConfiguration] {
    def writes(song: SingleSongToImageConverterJobConfiguration) = Json.obj(
      "songId" -> song.songId
    )
  }

  implicit val jobReads: Reads[Job] = (
    (JsPath \ "jobId").read[UUID] and
      (JsPath \ "jobName").read[String] and
      (JsPath \ "configuration").read[SingleSongToImageConverterJobConfiguration]) (Job.apply _)

  implicit val jobWrites = new Writes[Job] {
    def writes(job: Job) = Json.obj(
      "jobId" -> job.jobId,
      "jobName" -> job.jobName,
      "configuration" -> job.configuration)
  }

  def parseJobJson(json: String): Seq[Job] = {
    val parsedJson: JsValue = Json.parse(json)
    parsedJson.validate[Seq[Job]] match {
      case s: JsSuccess[Seq[Job]] => {
        val jobs: Seq[Job] = s.get
        if (jobs.length < 1) {
          throw new JobParsingException("Error while parsing jobs: No jobs found.")
        }
        jobs
      }
      case e: JsError => {
        throw new JobParsingException("Error while parsing jobs: " + JsError.toJson(e).toString())
      }
    }

  }
}