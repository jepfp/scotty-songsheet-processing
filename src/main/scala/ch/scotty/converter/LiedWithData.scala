package ch.scotty.converter

import java.time.LocalDateTime


sealed trait SourceSystem {
  def getIdentifier: String
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
  def concreteExtension: String
}

object FileType {

  def apply(extension: String): FileType = {
    val extensionNormalized = extension.toLowerCase
    extensionNormalized match {
      case "pdf" => Pdf()
      case "png" | "gif" | "jpg" | "jpeg" => Image(extensionNormalized)
      case _ => Unknown(extensionNormalized)
    }
  }

  case object Pdf {
    def apply(): FileType = Pdf("pdf")
  }

  case class Pdf(concreteExtension: String) extends FileType

  case class Image(concreteExtension: String) extends FileType

  case class Unknown(concreteExtension: String) extends FileType

}

case class LiedWithData(
                         sourceSystem: SourceSystem,
                         songId: Long,
                         title: String,
                         tonality: Option[String],
                         tags: List[String],
                         createdAt: LocalDateTime,
                         updatedAt: LocalDateTime,
                       //TODO: Make optional because we also want to have lyrics without pdfs...
                         data: java.sql.Blob,
                         fileType: FileType,
                         lyrics: Option[String]) {
}