package ch.scotty.converter

import java.io.{File => JFile}
import java.sql.Blob
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import better.files._
import ch.scotty._
import ch.scotty.converter.ConversionResults.Success
import ch.scotty.job.json.result.TestFolder
import com.typesafe.config.ConfigFactory
import org.scalatest.Matchers

import scala.collection.JavaConversions._

class ConditionalLiedPdfToImageConverterIntTest extends IntegrationSpec with TestFolder with Matchers {

  private val LIED_ID = 1
  private val LIEDERBUCH_ID = 2
  private val MNEMONIC = "mnemonic"
  private val LIEDERBUCH = "liederbuch"
  private val LIED_NR = "liednr"
  private val updatedAt = LocalDateTime.of(2014, 5, 29, 7, 22)

  private val convertible3pagesPdfResourceName = "convertible3pages"

  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  "convertPdfBlobToImage" should s"convert '${convertible3pagesPdfResourceName}' into the expected pictures and return Success" in {
    //arrange
    val testee = createTestee
    //act
    val result = testee.convertPdfBlobToImage(createLiedWithNumber(convertible3pagesPdfResourceName), createSongnumber)
    //assert
    assertExpectedAndActualPictureIsTheSame(generatePngFilename(convertible3pagesPdfResourceName, 0))
    assertExpectedAndActualPictureIsTheSame(generatePngFilename(convertible3pagesPdfResourceName, 1))
    assertExpectedAndActualPictureIsTheSame(generatePngFilename(convertible3pagesPdfResourceName, 2))
    assertResult(Success(None))(result)
  }

  def assertExpectedAndActualPictureIsTheSame(filename: String) = {
    val expectedFile: File = File(getClass.getResource(filename).toURI)
    val actualFile: File = new JFile(testFolder.getPath, filename).toScala
    actualFile.contentAsString shouldBe expectedFile.contentAsString
  }

  private def createTestee = {
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString))
    val exportPathResolverAndCreator = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap))
    new ConditionalLiedPdfToImageConverter(exportPathResolverAndCreator)
  }

  private def generatePngFilename(pdfResourceName: String, page: Int) = {
    s"${LIED_ID}-${updatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}-${page}.png"
  }

  private def createSongnumber = {
    Seq(Songnumber(LIED_ID, LIEDERBUCH_ID, MNEMONIC, LIEDERBUCH, LIED_NR))
  }

  private def createLiedWithNumber(pdfResourceName: String) = {
    LiedWithData(LIED_ID, pdfResourceName, Some("C"), LocalDateTime.MIN, updatedAt, readPdfSongResourceAsBlob(pdfResourceName))
  }

  def readPdfSongResourceAsBlob(prfResourceName: String): Blob = {
    val pdfUrl = getClass.getResource(prfResourceName + ".pdf")
    SongsheetTestUtils.readFileToBlob(pdfUrl)
  }

  it should s"not convert if the checksum of the source pdf and the checksum value in the table of content json match" in {
    //arrange
    val testee = createTestee
    val tocFilename = s"$LIED_ID.json"
    val currentTableOfContentFile: File = File(getClass.getResource(tocFilename).toURI)
    currentTableOfContentFile.copyTo(file"${testFolder.getPath}/$tocFilename")
    //act
    val result = testee.convertPdfBlobToImage(createLiedWithNumber(convertible3pagesPdfResourceName), createSongnumber)
    //assert
    testFolder.listFiles().length shouldBe 1
    Success(None)
  }

  it should s"not convert if checksums match but still export other song attributes" in {
    //arrange
    val testee = createTestee
    val tocFilename = s"$LIED_ID.json"
    val currentTableOfContentFile: File = File(getClass.getResource(tocFilename).toURI)
    val tocFile: File = file"${testFolder.getPath}/$tocFilename"
    currentTableOfContentFile.copyTo(tocFile)
    //act
    val changedUpdatedAtTime = LocalDateTime.now
    val result = testee.convertPdfBlobToImage(createLiedWithNumber(convertible3pagesPdfResourceName).copy(title = "changed title", updatedAt = changedUpdatedAtTime, tonality = Some("foo")), createSongnumber)
    //assert
    testFolder.listFiles().length shouldBe 1
    val contentOfUpdatedTocFile = tocFile.contentAsString
    contentOfUpdatedTocFile should include("changed title")
    contentOfUpdatedTocFile should include("foo")
    contentOfUpdatedTocFile should include(changedUpdatedAtTime.toString)
  }

}
