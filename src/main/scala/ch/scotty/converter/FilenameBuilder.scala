package ch.scotty.converter

private object FilenameBuilder {
  def build(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], sheetnumber: Integer): String = {
    val titelWithOnlyAllowedCharacters = liedWithData.title.replaceAll("[^a-zA-Z0-9äöüÄÖÜ .]", "")
    val songnumberString = songnumbers.map { x => x.mnemonic + x.liednr }.mkString("_")

    val filename = liedWithData.songId + "-" + sheetnumber + "-" + songnumberString + "-" + titelWithOnlyAllowedCharacters + "-" + ".png"
    filename
  }
}
