package ch.scotty.converter

import java.time.format.DateTimeFormatter

private object IdTimestampFilenameBuilder {
  def build(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], sheetnumber: Integer): String = {
    val filename = liedWithData.songId + "-" + formatUpdatedAtDate(liedWithData) +  "-" + sheetnumber + ".png"
    filename
  }

  def buildForTocEntry(liedWithData: LiedWithData): String = {
    val filename = liedWithData.songId + "-" + formatUpdatedAtDate(liedWithData) + ".json"
    filename
  }

  private def formatUpdatedAtDate(liedWithData: LiedWithData) = {
    liedWithData.updatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
  }
}
