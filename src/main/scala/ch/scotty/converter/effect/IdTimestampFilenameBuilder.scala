package ch.scotty.converter.effect

private[converter] object IdTimestampFilenameBuilder {
  def build(songId: Long, sheetnumber: Integer, versionTimestamp: String, outputFileType: String): String = {
    buildWithSuffix(songId, sheetnumber, versionTimestamp, "", outputFileType)
  }

  def buildWithSuffix(songId: Long, sheetnumber: Integer, versionTimestamp: String, filenameSuffix: String, outputFileType: String): String = {
    val suffix = if (filenameSuffix.isBlank) "" else ("-" + filenameSuffix)
    s"$songId-$versionTimestamp-$sheetnumber$suffix.$outputFileType"
  }

  def buildForTocEntry(songId: Long): String = {
    val filename = songId + ".json"
    filename
  }
}
