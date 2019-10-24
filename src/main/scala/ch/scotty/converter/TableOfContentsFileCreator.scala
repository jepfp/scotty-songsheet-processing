package ch.scotty.converter


import better.files._
import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversionWithException, Success}
import com.typesafe.scalalogging.Logger

private class TableOfContentsFileCreator(exportPathResolverAndCreator: ExportPathResolverAndCreator) {

  import play.api.libs.json._

  private implicit val songnumberWrites = Json.writes[TableOfContentsDTOs.Songnumber]
  private implicit val songWrites = Json.writes[TableOfContentsDTOs.Song]

  val logger = Logger(classOf[TableOfContentsFileCreator])

  def this() {
    this(new ExportPathResolverAndCreator())
  }

  def createFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceCheckSum : String, versionTimestamp : String): ConversionResult = {
    try {
      writeFile(liedWithData, songnumbers, amountOfPages, pdfSourceCheckSum, versionTimestamp)
    } catch {
      case e: Exception =>
        val m = s"Error while exporting table of contents: $liedWithData. Song is skipped."
        logger.warn(m, e)
        FailedConversionWithException(m + "Error: " + e, e)
    }
  }

  private def createEntry(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceChecksum : String, versionTimestamp : String) = {
    TableOfContentsDTOs.Song(liedWithData.songId,
      liedWithData.title,
      liedWithData.tonality,
      songnumbers.map(s => TableOfContentsDTOs.Songnumber(s.liederbuchId, s.mnemonic, s.liederbuch, s.liednr)),
      amountOfPages,
      pdfSourceChecksum,
      versionTimestamp,
      liedWithData.createdAt,
      liedWithData.updatedAt)
  }

  private def writeFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceCheckSum : String, versionTimestamp : String) = {
    val songInformation = createEntry(liedWithData, songnumbers, amountOfPages, pdfSourceCheckSum, versionTimestamp)
    val resultString = Json.prettyPrint(Json.toJson(songInformation))
    val file = File(generatePathString(liedWithData))
    file.overwrite(resultString)
    Success(None)
  }

  private def generatePathString(liedWithData: LiedWithData) = {
    val filename = IdTimestampFilenameBuilder.buildForTocEntry(liedWithData.songId)
    val path = exportPathResolverAndCreator.resolve(filename)
    path
  }
}



