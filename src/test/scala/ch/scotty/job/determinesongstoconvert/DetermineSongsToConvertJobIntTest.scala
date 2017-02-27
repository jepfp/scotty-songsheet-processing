package ch.scotty.job.determinesongstoconvert

import java.io.File
import java.util.UUID

import ch.scotty.fixture.SongFixture
import ch.scotty.job.json.result.TestFolder
import ch.scotty.{DatabaseConnection, IntegrationSpec}

class DetermineSongsToConvertJobIntTest extends IntegrationSpec with TestFolder  with DatabaseConnection{
  "findSongIdsToConvert" should "return the id of the song which is stored in the database" in {
    //arrange
    val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
    val jobConfig = new DetermineSongsToConvertJobConfiguration(UUID.fromString("cc07c7e8-f160-11e6-bc64-92361f002671"))
    val outputFilePath = new File(testFolder, "actualJobResults.json").getAbsolutePath
    val testee = createTestee(outputFilePath)
    //act
    val seqOfSongIds = testee.run(jobConfig)
    //assert
    val source = scala.io.Source.fromFile(new File(outputFilePath))
    val actualResult = try source.mkString finally source.close()
    assert(actualResult.contains("\"songId\" : " + createdLiedRow.id))
  }

  private def createTestee(path: String): DetermineSongsToConvertJob = {
    return new DetermineSongsToConvertJob {
      protected override lazy val jobsPath: String = path
    }
  }
}
