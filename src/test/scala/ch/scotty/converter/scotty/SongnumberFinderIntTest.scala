package ch.scotty.converter.scotty

import ch.scotty.{DatabaseConnection, IntegrationSpec}

class SongnumberFinderIntTest extends IntegrationSpec  with DatabaseConnection{

  "findSongnumbers" should "find the correct numbers for a songId" in {
    //arrange
    val praiseAdonaiRefSongId = 803
    val testee = new SongnumberFinder()
    //act
    val songnumbers = testee.findSongnumbers(praiseAdonaiRefSongId)
    //assert
    assertResult(Seq("129", "53", "4"), "expected to find these 3 songnumbers in here")(songnumbers.map(_.liednr))
  }

}
