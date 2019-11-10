package ch.scotty.converter.effect

import better.files._
import ch.scotty.converter.SourceSystem

private[converter] class TableOfContentsFileReader(exportPathResolverAndCreator: ExportPathResolverAndCreator) {

  import TableOfContentsDTOs.Formats._
  import play.api.libs.json._

  def this() {
    this(new ExportPathResolverAndCreator())
  }

  def readTableOfContentsFile(sourceSystem: SourceSystem, id: Long): Either[String, TableOfContentsDTOs.Song] = {
    val path = generatePathString(sourceSystem, id)
    try {
      val file: File = File(path)
      Json.fromJson[TableOfContentsDTOs.Song](Json.parse(file.contentAsString())) match {
        case JsSuccess(song, _) =>
          Right(song)
        case e@JsError(_) =>
          Left(JsError.toJson(e).toString())
      }

    } catch {
      case e: Exception =>
        Left("Error while reading json file " + path + ". Error: " + e)
    }
  }

  private def generatePathString(sourceSystem: SourceSystem, songId: Long) = {
    val filename = IdTimestampFilenameBuilder.buildForTocEntry(songId)
    exportPathResolverAndCreator.resolve(sourceSystem, filename)
  }
}



