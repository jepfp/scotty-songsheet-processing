package ch.scotty.converter.effect

import better.files._
import ch.scotty.converter.{LiedWithData, Songnumber}
import com.typesafe.scalalogging.Logger

import scala.util.Try

private[converter] class TableOfContentsFileCreator(exportPathResolverAndCreator: ExportPathResolverAndCreator) {

  import play.api.libs.json._

  private implicit val songnumberWrites = Json.writes[TableOfContentsDTOs.Songnumber]
  private implicit val songWrites = Json.writes[TableOfContentsDTOs.Song]

  val logger = Logger(classOf[TableOfContentsFileCreator])

  def this() {
    this(new ExportPathResolverAndCreator())
  }

  def createFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceCheckSum: String, versionTimestamp: String, fileTypeAfterExport : String): Try[TableOfContentsDTOs.Song] = {
    Try(writeFile(liedWithData, songnumbers, amountOfPages, pdfSourceCheckSum, versionTimestamp, fileTypeAfterExport))
  }

  private def writeFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceCheckSum: String, versionTimestamp: String, fileTypeAfterExport : String) = {
    val songInformation = createEntry(liedWithData, songnumbers, amountOfPages, pdfSourceCheckSum, versionTimestamp, fileTypeAfterExport)
    val resultString = Json.prettyPrint(Json.toJson(songInformation))
    val file = File(generatePathString(liedWithData))
    file.overwrite(resultString)
    songInformation
  }

  private def createEntry(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceChecksum: String, versionTimestamp: String, fileTypeAfterExport : String) = {
    TableOfContentsDTOs.Song(
      liedWithData.sourceSystem.getIdentifier,
      liedWithData.songId,
      liedWithData.title,
      liedWithData.tonality,
      songnumbers.map(s => TableOfContentsDTOs.Songnumber(s.liederbuchId, s.mnemonic, s.liederbuch, s.liednr)),
      liedWithData.tags,
      amountOfPages,
      pdfSourceChecksum,
      versionTimestamp,
      liedWithData.createdAt,
      liedWithData.updatedAt,
      fileTypeAfterExport,
      liedWithData.lyrics)
  }

  private def generatePathString(liedWithData: LiedWithData) = {
    val filename = IdTimestampFilenameBuilder.buildForTocEntry(liedWithData.songId)
    val path = exportPathResolverAndCreator.resolve(liedWithData.sourceSystem, filename)
    path
  }
}



