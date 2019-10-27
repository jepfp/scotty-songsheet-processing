package ch.scotty.converter

private object IdTimestampFilenameBuilder {
  def build(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], sheetnumber: Integer, versionTimestamp: String): String = {
    val filename = liedWithData.songId + "-" + versionTimestamp + "-" + sheetnumber + ".png"
    filename
  }

  def buildForTocEntry(songId: Long): String = {
    val filename = songId + ".json"
    filename
  }
}
