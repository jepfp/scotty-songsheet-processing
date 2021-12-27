package ch.scotty.converter.effect

import better.files._
import ch.scotty.converter._
import ch.scotty._
import org.scalatest.TryValues
import org.scalatest.matchers.should.Matchers

import java.time.LocalDateTime


class TableOfContentsFileCreatorIntTest extends IntegrationSpec with TestFolder with Matchers with TryValues {

  private val LIED_ID = 1
  private val LIEDERBUCH_ID = 2
  private val MNEMONIC = "mnemonic"
  private val LIEDERBUCH = "liederbuch"
  private val LIED_NR = "liednr"
  private val updatedAt = LocalDateTime.of(2014, 5, 29, 7, 22)
  private val fileTypeAfterExport = "jpeg"


  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  "createFile" should s"convert into the expected json and return Success" in {
    //arrange
    val testee = createTestee
    //act
    val result = testee.createFile(createLiedWithNumber(), createSongnumber, 3, "2745913656", "20190909121340", fileTypeAfterExport)
    //assert
    assertExpectedAndActualJsonIsTheSame(generateJsonFilename())
    result.success.value.songId shouldBe LIED_ID
  }

  def assertExpectedAndActualJsonIsTheSame(filename: String) = {
    val expectedFile: File = File(getClass.getResource(filename).toURI)
    val actualFile: File = File(testFolder.getPath, SourceSystem.Scotty.getIdentifier, filename)
    expectedFile.contentAsString shouldBe actualFile.contentAsString
  }

  private def createTestee = {
    val exportPathResolverAndCreator = new ExportPathResolverAndCreator(SongsheetConfig.get(Map("scotty-songsheet-processing.exportBaseDir" -> testFolder.getPath)))
    new TableOfContentsFileCreator(exportPathResolverAndCreator)
  }

  private def generateJsonFilename() = {
    s"${LIED_ID}.json"
  }

  private def createSongnumber = {
    Seq(Songnumber(LIED_ID, LIEDERBUCH_ID, MNEMONIC, LIEDERBUCH, LIED_NR))
  }

  private def createLiedWithNumber() = {
    LiedWithData(SourceSystem.Scotty, LIED_ID, "foo", Some("C"), List.empty, LocalDateTime.MIN, updatedAt, null, FileType.Pdf(), None)
  }

}
