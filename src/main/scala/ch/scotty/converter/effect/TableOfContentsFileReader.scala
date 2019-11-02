package ch.scotty.converter.effect

import better.files._

private[converter] class TableOfContentsFileReader(exportPathResolverAndCreator: ExportPathResolverAndCreator) {

  import play.api.libs.json._

  private implicit val songnumberFormat: OFormat[TableOfContentsDTOs.Songnumber] = Json.format[TableOfContentsDTOs.Songnumber]
  private implicit val songFormat: OFormat[TableOfContentsDTOs.Song] = Json.format[TableOfContentsDTOs.Song]

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



