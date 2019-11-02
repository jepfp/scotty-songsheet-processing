package ch.scotty.converter.effect

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private[converter] class VersionStringCreator() {


  def createVersionString(timestamp: LocalDateTime) = {
    timestamp.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
  }

}