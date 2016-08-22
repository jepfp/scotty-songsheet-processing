package ch.scotty.job.json.result

import scala.collection.mutable

object PerJobDefinitionResultHolder{
  class Builder{
    private val success = mutable.MutableList[Success]()
    private val failure = mutable.MutableList[Failure]()

    def addAnEitherResult(executionResult: Either[Failure, Success]) = {
      executionResult match {
        case Right(s) => success += s
        case Left(f) => failure += f
      }
    }

    def build() : PerJobDefinitionResultHolder = new PerJobDefinitionResultHolder(success.toSeq, failure.toSeq)
  }
}

class PerJobDefinitionResultHolder(val success : Seq[Success], val failure : Seq[Failure]) {

}
