package ch.scotty.job.json.result

import java.io.File

import ch.scotty.util.FilePrinter
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
    println(s"Writing ${resultFile.getPath}")
    FilePrinter.print(resultFile)(_.println(resultString))
  }
}
