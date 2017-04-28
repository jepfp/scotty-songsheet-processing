package ch.scotty.converter

object ConversionResults {

  sealed trait ConversionResult

  case class Success() extends ConversionResult

  case class FailedConversion(message: String, exception: Exception) extends ConversionResult

}
