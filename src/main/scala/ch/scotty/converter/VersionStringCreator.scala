package ch.scotty.converter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private class VersionStringCreator() {


  def createVersionString(timestamp: LocalDateTime) = {
    timestamp.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
  }

}