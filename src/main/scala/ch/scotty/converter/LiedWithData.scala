package ch.scotty.converter

import java.time.LocalDateTime


sealed trait SourceSystem

object SourceSystem {

  case object Scotty extends SourceSystem

  case object Songanize extends SourceSystem

}

sealed trait FileType {
  def extensions: List[String]

  def toOptionIfMatchesTypeString(typeAsString: String): Option[FileType] = {
    if (extensions.contains(typeAsString.toLowerCase)) Some(this) else None
  }
}

object FileType {

  case object Pdf extends FileType {
    override def extensions: List[String] = List("pdf")
  }

  case object Image extends FileType {
    override def extensions: List[String] = List("png", "gif", "jpg", "jpeg")
  }

  case class Unknown(unknownType : String) extends FileType {
    override def extensions: List[String] = List.empty

  }


  def fromString(typeAsString: String): FileType = {
    Pdf.toOptionIfMatchesTypeString(typeAsString)
      .orElse(Image.toOptionIfMatchesTypeString(typeAsString))
      .getOrElse(Unknown(typeAsString))
  }

}

case class LiedWithData(
                         sourceSystem: SourceSystem,
                         songId: Long,
                         title: String,
                         tonality: Option[String],
                         tags: List[String],
                         createdAt: LocalDateTime,
                         updatedAt: LocalDateTime,
                         data: java.sql.Blob,
                         fileType: FileType) {
}