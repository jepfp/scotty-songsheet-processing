package ch.scotty.converter

object ConversionResults {

  sealed trait ConversionResult

  case class Success(detailMessage : Option[String]) extends ConversionResult

  case class FailedConversionWithException(message: String, exception: Exception) extends ConversionResult

  case class FailedConversion(message: String) extends ConversionResult

}
