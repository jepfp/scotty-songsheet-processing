package ch.scotty.converter

import ch.scotty.fixture.SongFixture
import ch.scotty.{DatabaseConnection, IntegrationSpec, SongsheetTestUtils}
import org.apache.commons.io.IOUtils
import org.scalatest.EitherValues

class LiedSourcePdfFileFinderIntTest extends IntegrationSpec with DatabaseConnection with EitherValues {


  "findFile" should "return a LiedWithData object with all attributes and the binary data" in {
    //arrange
    val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
    val testee = new LiedSourcePdfFileFinder()
    //act
    val liedWithDataEither = testee.findFile(createdLiedRow.id)
    //assert
    val liedWithData = liedWithDataEither.right.value
    assertResult(createdLiedRow.titel)(liedWithData.titel)
    assertResult(true, "Content of both blobs must be equals.")(IOUtils.contentEquals(SongsheetTestUtils.readFileToBlob(SongFixture.DefaultSongFixture.pdfUrl).getBinaryStream, liedWithData.data.getBinaryStream()))
  }
}
