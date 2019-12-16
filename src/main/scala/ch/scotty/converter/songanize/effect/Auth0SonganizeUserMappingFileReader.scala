package ch.scotty.converter.songanize.effect

import better.files._

import scala.util.{Failure, Success, Try}

class Auth0SonganizeUserMappingFileReader(exportPathResolverAndCreator: Auth0SonganizeUserMappingPathResolverAndCreator) {

  import Auth0SonganizeUserMapping.Formats._
  import play.api.libs.json._

  def this() {
    this(new Auth0SonganizeUserMappingPathResolverAndCreator())
  }

  def readMappingFile(): Try[Seq[Auth0SonganizeUserMapping]] = {
    val path = exportPathResolverAndCreator.resolve()
    Try {
      File(path).contentAsString
    }.flatMap { contentAsString =>
      Json.fromJson[Seq[Auth0SonganizeUserMapping]](Json.parse(contentAsString)) match {
        case JsSuccess(mapping, _) =>
          Success(mapping)
        case e@JsError(_) =>
          Failure(new Exception(JsError.toJson(e).toString()))
      }
    }
  }
}



