package ch.scotty.converter.scotty

import ch.scotty.fixture.SongFixture
import ch.scotty.{DatabaseConnection, IntegrationSpec}

class SongIdsToConvertFinderIntTest extends IntegrationSpec with DatabaseConnection {
  "findSongIdsToConvert" should "return the id of the song which is stored in the database" in {
    //arrange
    val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
    val testee = new SongIdsToConvertFinder()
    //act
    val seqOfSongIds = testee.findSongIdsToConvert()
    //assert
    assertResult(createdLiedRow.id)(seqOfSongIds.head)
  }

  it should "return an empty list if no songIds are found" in {
    //arrange
    val testee = new SongIdsToConvertFinder()
    //act
    val seqOfSongIds = testee.findSongIdsToConvert()
    //assert
    assertResult(Seq.empty)(seqOfSongIds)
  }
}

