package ch.scotty.converter.songanize.effect

import ch.scotty._
import play.api.libs.json.{JsSuccess, Json}

class Auth0SonganizeUserMappingTest extends UnitSpec  {

  import Auth0SonganizeUserMapping.Formats._

  "mapping" should "read and wirte a sequence of mappings" in {
    //arrange
    val mappings = Seq(
      Auth0SonganizeUserMapping("fooIdentifier", 1),
      Auth0SonganizeUserMapping("barIdentifier", 2))
    //act
    val resultString = Json.prettyPrint(Json.toJson(mappings))
    //assert
    Json.fromJson[Seq[Auth0SonganizeUserMapping]](Json.parse(resultString)) shouldBe JsSuccess(mappings)
  }


}
