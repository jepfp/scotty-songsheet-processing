package ch.scotty.converter.scotty

import ch.scotty.fixture.SongFixture
import ch.scotty.{DatabaseConnection, IntegrationSpec, SongsheetTestUtils}
import org.apache.commons.io.IOUtils
import org.scalatest.TryValues

class LiedSourcePdfFileFinderIntTest extends IntegrationSpec with DatabaseConnection with TryValues {


  "findFile" should "return a LiedWithData object with all attributes and the binary data" in {
    //arrange
    val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
    val testee = new LiedSourcePdfFileFinder()
    //act
    val liedWithDataTry = testee.performQuery(createdLiedRow.id)
    //assert
    val liedWithData = liedWithDataTry.success.value
    assertResult(createdLiedRow.titel)(liedWithData.title)
    assertResult(true, "Content of both blobs must be equals.")(IOUtils.contentEquals(SongsheetTestUtils.readFileToBlob(SongFixture.DefaultSongFixture.pdfUrl).getBinaryStream, liedWithData.data.getBinaryStream()))
  }
}
