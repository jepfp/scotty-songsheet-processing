package ch.scotty.converter.effect

import ch.scotty.converter.{LiedWithData, Songnumber}

private[converter] object IdTimestampFilenameBuilder {
  def build(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], sheetnumber: Integer, versionTimestamp: String): String = {
    val filename = liedWithData.songId + "-" + versionTimestamp + "-" + sheetnumber + ".png"
    filename
  }

  def buildForTocEntry(songId: Long): String = {
    val filename = songId + ".json"
    filename
  }
}
