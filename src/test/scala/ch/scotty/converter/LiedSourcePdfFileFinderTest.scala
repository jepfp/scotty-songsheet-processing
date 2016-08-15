package ch.scotty.converter

import ch.scotty.fixture.SongBuilder._
import ch.scotty.fixture.SongFixture._
import ch.scotty.fixture.{SongFixture, Tonality}
import ch.scotty.generatedschema.Tables
import ch.scotty.{IntegrationSpec, SongsheetTestUtils}
import org.apache.commons.io.IOUtils

class LiedSourcePdfFileFinderTest extends IntegrationSpec {


  "findFile" should "return a LiedWithData object with all attributes and the binary data" in {
    //arrange
    val aSong = songBuilder withTitle "Revelation Song" withSectionId 1 withLastEditUserId 1 withTonality Tonality.CA build()
    val pdfUrl = getClass.getResource("revelationSong.pdf")
    val aFile = SongFixture.InsertableFile(pdfUrl)
    val createdRows: Map[Class[_ <: AnyRef], AnyRef] = songFixtureBuilder withSong aSong withFile aFile buildAndExecute;
    val createdLiedRow = createdRows.get(classOf[Tables.LiedRow]).get.asInstanceOf[Tables.LiedRow]
    val testee = new LiedSourcePdfFileFinder()
    //act
    val liedWithData = testee.findFile(createdLiedRow.id)
    //assert
    assertResult(aSong.titel)(liedWithData.titel)
    assertResult(true, "Content of both blobs must be equals.")(IOUtils.contentEquals(SongsheetTestUtils.readFileToBlob(pdfUrl).getBinaryStream, liedWithData.data.getBinaryStream()))

  }
}
