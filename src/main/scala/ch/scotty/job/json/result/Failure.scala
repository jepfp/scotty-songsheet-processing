package ch.scotty.job.json.result

import java.util.UUID

case class Failure(val jobId: UUID, val message: String, val exceptions: Seq[Exception]) {

  def getExceptionStrings: Seq[String] = {
    exceptions.map(_.getMessage)
  }

}
