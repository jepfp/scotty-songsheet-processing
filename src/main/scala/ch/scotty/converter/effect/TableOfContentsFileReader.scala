package ch.scotty.converter.effect

import better.files._

private[converter] class TableOfContentsFileReader(exportPathResolverAndCreator: ExportPathResolverAndCreator) {

  import TableOfContentsDTOs.Formats._
  import play.api.libs.json._

  def this() {
    this(new ExportPathResolverAndCreator())
  }

  def readTableOfContentsFile(id: Long): Either[String, TableOfContentsDTOs.Song] = {
    try {
      val file: File = File(generatePathString(id))
      Json.fromJson[TableOfContentsDTOs.Song](Json.parse(file.contentAsString())) match {
        case JsSuccess(song, _) =>
          Right(song)
        case e@JsError(_) =>
          Left(JsError.toJson(e).toString())
      }

    } catch {
      case e: Exception =>
        Left("Error while reading json file " + generatePathString(id) + ". Error: " + e)
    }
  }

  private def generatePathString(songId: Long) = {
    val filename = IdTimestampFilenameBuilder.buildForTocEntry(songId)
    exportPathResolverAndCreator.resolve(filename)
  }
}



