package ch.scotty.job

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError
import java.util.UUID

class JobParser {
  implicit val jobReads: Reads[Job] = (
    (JsPath \ "jobId").read[UUID] and
    (JsPath \ "liedId").read[Long] and
    (JsPath \ "output").read[String])(Job.apply _)

  implicit val jobWrites = new Writes[Job] {
    def writes(job: Job) = Json.obj(
      "jobId" -> job.jobId,
      "liedId" -> job.liedId,
      "output" -> job.output)
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