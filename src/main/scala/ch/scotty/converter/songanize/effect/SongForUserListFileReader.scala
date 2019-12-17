package ch.scotty.converter.songanize.effect

import better.files._

import scala.util.{Failure, Success, Try}

class SongForUserListFileReader(exportPathResolverAndCreator: SongForUserListExportPathResolverAndCreator) {

  import SongForUser.Formats._
  import play.api.libs.json._

  def this() {
    this(new SongForUserListExportPathResolverAndCreator())
  }

  def readMappingFile(userId : Long): Try[Seq[SongForUser]] = {
    val path = exportPathResolverAndCreator.resolve(userId)
    Try {
      File(path).contentAsString
    }.flatMap { contentAsString =>
      Json.fromJson[Seq[SongForUser]](Json.parse(contentAsString)) match {
        case JsSuccess(mapping, _) =>
          Success(mapping)
        case e@JsError(_) =>
          Failure(new Exception(JsError.toJson(e).toString()))
      }
    }
  }
}



