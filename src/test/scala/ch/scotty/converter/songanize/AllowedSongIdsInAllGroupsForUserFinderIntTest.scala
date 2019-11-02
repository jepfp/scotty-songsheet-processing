package ch.scotty.converter.songanize

import ch.scotty.IntegrationSpec
import org.scalatest.EitherValues
import org.scalatest.Matchers._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class AllowedSongIdsInAllGroupsForUserFinderIntTest extends IntegrationSpec with SonganizeDatabaseConnection with EitherValues {


  "AllowedSongIdsInAllGroupsForUserFinder" should "return return all songIds with the belonging group to which a user has access" in {
    //arrange
    val testee = new AllowedSongIdsInAllGroupsForUserFinder()
    //act
    val result = Await.result(testee.find(70), Duration.Inf)
    //assert
    result shouldBe Vector(
      SongForUser("Adoray", 961),
      SongForUser("New SonganizeShip", 804),
      SongForUser("New SonganizeShip", 814),
      SongForUser("New SonganizeShip", 811),
      SongForUser("Private", 961),
      SongForUser("Private", 967),
      SongForUser("Private", 1133))
  }
}
