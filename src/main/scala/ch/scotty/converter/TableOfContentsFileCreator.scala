package ch.scotty.converter


import better.files._
import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversionWithException, Success}

private class TableOfContentsFileCreator(exportPathResolverAndCreator: ExportPathResolverAndCreator) {
  import play.api.libs.json._

  private implicit val songnumberWrites = Json.writes[TableOfContentsDTOs.Songnumber]
  private implicit val songWrites = Json.writes[TableOfContentsDTOs.Song]

  def this() {
    this(new ExportPathResolverAndCreator())
  }

  def createFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]): ConversionResult = {
    try {
      writeFile(liedWithData, songnumbers)
    } catch {
      case e: Exception =>
        FailedConversionWithException("Error while exporting " + liedWithData + ". Song is skipped. Error: " + e, e)
    }
  }

  private def createEntry(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {
    TableOfContentsDTOs.Song(liedWithData.songId,
      liedWithData.title,
      liedWithData.tonality,
      songnumbers.map(s => TableOfContentsDTOs.Songnumber(s.liederbuchId, s.mnemonic, s.liederbuch, s.liednr)),
      liedWithData.createdAt,
      liedWithData.updatedAt)
  }


  private def writeFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {
    val songInformation = createEntry(liedWithData, songnumbers)
    val resultString = Json.prettyPrint(Json.toJson(songInformation))
    val file = File(generatePathString(liedWithData))
    file.overwrite(resultString)
    Success()
  }

  private def generatePathString(liedWithData: LiedWithData) = {
    val filename = IdTimestampFilenameBuilder.buildForTocEntry(liedWithData)
    val path = exportPathResolverAndCreator.resolve(filename)
    path
  }
}


