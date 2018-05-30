package ch.scotty.converter

import java.io.{File => JFile}
import java.sql.Blob
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import better.files._
import ch.scotty._
import ch.scotty.converter.ConversionResults.{FailedConversionWithException, Success}
import ch.scotty.job.json.result.TestFolder
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConversions._

class LiedPdfToImageConverterIntTest extends IntegrationSpec with TestFolder {

  private val LIED_ID = 1
  private val LIEDERBUCH_ID = 2
  private val MNEMONIC = "mnemonic"
  private val LIEDERBUCH = "liederbuch"
  private val LIED_NR = "liednr"
  private val updatedAt = LocalDateTime.of(2014, 5, 29, 7, 22)

  private val convertible3pagesPdfResourceName = "convertible3pages"
  private val pdfWithNotLinkableProfile = "pdfWithNotLinkableProfile"

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
    assertResult(Success())(result)
  }

  def assertExpectedAndActualPictureIsTheSame(filename: String) = {
    val expectedFile: File = File(getClass.getResource(filename).toURI)
    val actualFile: File = new JFile(testFolder.getPath, filename).toScala
    expectedFile === actualFile
    assert(expectedFile.isSameContentAs(actualFile))
  }

  private def createTestee = {
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString))
    val exportPathResolverAndCreator = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap))
    new LiedPdfToImageConverter(exportPathResolverAndCreator)
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

  it should s"return ConversionFailed when '${pdfWithNotLinkableProfile}' is being converted" in {
    //arrange
    val testee = createTestee
    //act
    val result = testee.convertPdfBlobToImage(createLiedWithNumber(pdfWithNotLinkableProfile), createSongnumber)
    //assert
    result match {
      case FailedConversionWithException(message: String, exception) => {
        assert(message.contains("Error while exporting"))
      }
      case _ => {
        fail("Was not ConversionFailed")
      }
    }
  }

}