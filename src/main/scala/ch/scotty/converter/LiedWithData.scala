package ch.scotty.converter

import java.time.LocalDateTime


sealed trait SourceSystem{
  def getIdentifier : String
}

object SourceSystem {

  case object Scotty extends SourceSystem {
    override def getIdentifier: String = "scotty"
  }

  case object Songanize extends SourceSystem {
    override def getIdentifier: String = "songanize"
  }

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