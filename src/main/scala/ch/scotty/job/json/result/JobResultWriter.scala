package ch.scotty.job.json.result

import java.io.File

import play.api.libs.json.{JsObject, Json, Writes}

object JobResultWriter {

  private implicit val successWrites = new Writes[Success] {
    def writes(success: Success): JsObject = Json.obj(
      "jobId" -> success.jobId)
  }

  implicit val failureWrites = new Writes[Failure] {
    def writes(failure: Failure): JsObject = Json.obj(
      "jobId" -> failure.jobId,
      "message" -> failure.message,
      "exceptions" -> failure.getExceptionStrings)
  }

  private implicit val perJobDefinitionResultHolderWrites = new Writes[PerJobDefinitionResultHolder] {
    def writes(perJobDefinitionResultHolder: PerJobDefinitionResultHolder): JsObject = Json.obj(
      "success" -> perJobDefinitionResultHolder.success,
      "failure" -> perJobDefinitionResultHolder.failure)
  }

  def writeJobResults(resultFile: File, jobResults: Map[String, PerJobDefinitionResultHolder]): Unit = {
    val resultString = Json.prettyPrint(Json.toJson(jobResults))
    printToFile(resultFile)(p => p.println(resultString))
  }

  private def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try {
      op(p)
    } finally {
      p.close()
    }
  }
}
