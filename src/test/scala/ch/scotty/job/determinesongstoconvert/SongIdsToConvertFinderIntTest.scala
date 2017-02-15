package ch.scotty.job.determinesongstoconvert

import ch.scotty.IntegrationSpec
import ch.scotty.fixture.SongFixture

class SongIdsToConvertFinderIntTest extends IntegrationSpec {
  "findSongIdsToConvert" should "return the id of the song which is stored in the database" in {
    //arrange
    val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
    val testee = new SongIdsToConvertFinder()
    //act
    val seqOfSongIds = testee.findSongIdsToConvert()
    //assert
    assertResult(createdLiedRow.id)(seqOfSongIds.head)
  }
}

