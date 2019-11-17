package ch.scotty.converter.effect

private[converter] object IdTimestampFilenameBuilder {
  def build(songId: Long, sheetnumber: Integer, versionTimestamp: String, outputFileType : String): String = {
    val filename = songId + "-" + versionTimestamp + "-" + sheetnumber + "." + outputFileType
    filename
  }

  def buildForTocEntry(songId: Long): String = {
    val filename = songId + ".json"
    filename
  }
}
