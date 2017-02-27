package ch.scotty.converter

import ch.scotty.fixture.SongFixture
import ch.scotty.{DatabaseConnection, IntegrationSpec, SongsheetTestUtils}
import org.apache.commons.io.IOUtils

class LiedSourcePdfFileFinderIntTest extends IntegrationSpec with DatabaseConnection {


  "findFile" should "return a LiedWithData object with all attributes and the binary data" in {
    //arrange
    val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
    val testee = new LiedSourcePdfFileFinder()
    //act
    val liedWithData = testee.findFile(createdLiedRow.id)
    //assert
    assertResult(createdLiedRow.titel)(liedWithData.titel)
    assertResult(true, "Content of both blobs must be equals.")(IOUtils.contentEquals(SongsheetTestUtils.readFileToBlob(SongFixture.DefaultSongFixture.pdfUrl).getBinaryStream, liedWithData.data.getBinaryStream()))
  }
}
