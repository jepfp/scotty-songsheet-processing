package ch.scotty.converter.songanize.effect

import play.api.libs.json.{Json, OFormat}

object Auth0SonganizeUserMapping {

  object Formats {
    implicit val auth0SonganizeUserMappingFormat: OFormat[Auth0SonganizeUserMapping] = Json.format[Auth0SonganizeUserMapping]
  }

}

case class Auth0SonganizeUserMapping(auth0Identifier: String, userId: Long)
