package ch.scotty.job.json.result

import java.util.UUID

import play.api.libs.json._

class JobResultMarshaller {

  implicit val successWrites = new Writes[Success] {
    def writes(success: Success) = Json.obj(
      "jobId" -> success.jobId)
  }

  implicit val failureWrites = new Writes[Failure] {
    def writes(failure: Failure) = Json.obj(
      "jobId" -> failure.jobId,
      "message" -> failure.message,
      "exceptions" -> failure.getExceptionStrings)
  }

  def parseJobJson(json: String): String = {
    val json = Json.toJson(new Success(jobId = UUID.randomUUID()))
    json.toString()
  }
}