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

  def createFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceCheckSum: String, versionTimestamp: String): Try[TableOfContentsDTOs.Song] = {
    Try(writeFile(liedWithData, songnumbers, amountOfPages, pdfSourceCheckSum, versionTimestamp))
  }

  private def writeFile(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceCheckSum: String, versionTimestamp: String) = {
    val songInformation = createEntry(liedWithData, songnumbers, amountOfPages, pdfSourceCheckSum, versionTimestamp)
    val resultString = Json.prettyPrint(Json.toJson(songInformation))
    val file = File(generatePathString(liedWithData))
    file.overwrite(resultString)
    songInformation
  }

  private def createEntry(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], amountOfPages: Int, pdfSourceChecksum: String, versionTimestamp: String) = {
    TableOfContentsDTOs.Song(liedWithData.songId,
      liedWithData.title,
      liedWithData.tonality,
      songnumbers.map(s => TableOfContentsDTOs.Songnumber(s.liederbuchId, s.mnemonic, s.liederbuch, s.liednr)),
      liedWithData.tags,
      liedWithData.sourceSystem.getIdentifier,
      amountOfPages,
      pdfSourceChecksum,
      versionTimestamp,
      liedWithData.createdAt,
      liedWithData.updatedAt)
  }

  private def generatePathString(liedWithData: LiedWithData) = {
    val filename = IdTimestampFilenameBuilder.buildForTocEntry(liedWithData.songId)
    val path = exportPathResolverAndCreator.resolve(liedWithData.sourceSystem, filename)
    path
  }
}



