package ch.scotty.job.json

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

abstract class JobParser[T] {
  protected implicit def configurationReads: Reads[T]

  protected implicit def configurationWrites: Writes[T]

  implicit val jobReads: Reads[JsonJob[T]] = (
    (JsPath \ "jobId").read[UUID] and
      (JsPath \ "jobName").read[String] and
      (JsPath \ "configuration").read[T]) (JsonJob.apply[T] _)

  implicit val jobWrites = new Writes[JsonJob[T]] {
    def writes(job: JsonJob[T]) = Json.obj(
      "jobId" -> job.jobId,
      "jobName" -> job.jobName,
      "configuration" -> job.configuration)
  }

  def parseJobJson(json: String): Seq[JsonJob[T]] = {
    val parsedJson: JsValue = Json.parse(json)
    parsedJson.validate[Seq[JsonJob[T]]] match {
      case s: JsSuccess[Seq[JsonJob[T]]] => {
        val jobs: Seq[JsonJob[T]] = s.get
        if (jobs.length < 1) {
          throw new JobParsingException("No jobs found.")
        }
        jobs
      }
      case e: JsError => {
        throw new JobParsingException("Error while parsing jobs: " + JsError.toJson(e).toString())
      }
    }

  }
}