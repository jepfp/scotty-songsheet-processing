package ch.scotty.converter

import java.sql.Blob
import java.time.LocalDateTime

import better.files._
import ch.scotty.converter.effect.ExportPathResolverAndCreator
import ch.scotty.{TestFolder, _}
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, TryValues}

import scala.collection.JavaConversions._

class ConverterExporterIntTest extends IntegrationSpec with TestFolder with Matchers with TryValues {

  private val LIED_ID = 1
  private val LIEDERBUCH_ID = 2
  private val MNEMONIC = "mnemonic"
  private val LIEDERBUCH = "liederbuch"
  private val LIED_NR = "liednr"
  private val updatedAt = LocalDateTime.of(2014, 5, 29, 7, 22)
  val STUBBED_VERSION_STRING = "2745913656"

  private val convertible3pagesPdfResourceName = "convertible3pages"

  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  "convertPdfBlobToImage" should s"convert '${convertible3pagesPdfResourceName}' into the expected pictures and return Success" in {
    //arrange
    val testee = createTestee
    //act
    val result = testee.convertAndExport(createLiedWithNumber(convertible3pagesPdfResourceName), createSongnumber)
    //assert
    assertExpectedAndActualPictureIsTheSame(generatePngFilename(convertible3pagesPdfResourceName, 0))
    assertExpectedAndActualPictureIsTheSame(generatePngFilename(convertible3pagesPdfResourceName, 1))
    assertExpectedAndActualPictureIsTheSame(generatePngFilename(convertible3pagesPdfResourceName, 2))
    result.success.value.songId shouldBe LIED_ID
  }

  def assertExpectedAndActualPictureIsTheSame(filename: String) = {
    println(s"load file from resource for test: filename=$filename class=${getClass}")

    val resource = getClass.getResource(filename)
    val expectedFile: File = File(resource.toURI)
    val actualFile: File = File(testFolder.getPath, SourceSystem.Scotty.getIdentifier, filename)
    actualFile.contentAsString shouldBe expectedFile.contentAsString
  }

  private def createTestee = {
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString))
    val exportPathResolverAndCreator = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap))
    new ConverterExporter(exportPathResolverAndCreator)
  }

  private def generatePngFilename(pdfResourceName: String, page: Int) = {
    s"${LIED_ID}-${STUBBED_VERSION_STRING}-${page}.png"
  }

  private def createSongnumber = {
    Seq(Songnumber(LIED_ID, LIEDERBUCH_ID, MNEMONIC, LIEDERBUCH, LIED_NR))
  }

  private def createLiedWithNumber(pdfResourceName: String) = {
    LiedWithData(SourceSystem.Scotty, LIED_ID, pdfResourceName, Some("C"), List.empty, LocalDateTime.MIN, updatedAt, readPdfSongResourceAsBlob(pdfResourceName), FileType.Pdf())
  }

  def readPdfSongResourceAsBlob(prfResourceName: String): Blob = {
    val pdfUrl = getClass.getResource(prfResourceName + ".pdf")
    SongsheetTestUtils.readFileToBlob(pdfUrl)
  }

  it should s"not convert if the checksum of the source pdf and the checksum value in the table of content json match" in {
    //arrange
    val testee = createTestee
    copyToCFileToTempDir()
    //act
    testee.convertAndExport(createLiedWithNumber(convertible3pagesPdfResourceName), createSongnumber)
    //assert
    testFolder.listFiles().length shouldBe 1
  }

  private def copyToCFileToTempDir() = {
    val tocFilename = s"$LIED_ID.json"
    val currentTableOfContentFile: File = File(getClass.getResource(tocFilename).toURI)
    val destinationFile = file"${testFolder.getPath}/${SourceSystem.Scotty.getIdentifier}"
    destinationFile.createDirectoryIfNotExists(true)
    currentTableOfContentFile.copyToDirectory(destinationFile)
    destinationFile./(tocFilename)
  }

  it should s"not convert if checksums match but still export other song attributes" in {
    //arrange
    val testee = createTestee
    val tocFile: File = copyToCFileToTempDir()
    //act
    val changedUpdatedAtTime = LocalDateTime.of(2019, 11, 2, 22, 14, 2)
    testee.convertAndExport(createLiedWithNumber(convertible3pagesPdfResourceName).copy(title = "changed title", updatedAt = changedUpdatedAtTime, tonality = Some("foo")), createSongnumber)
    //assert
    testFolder.listFiles().length shouldBe 1
    val contentOfUpdatedTocFile = tocFile.contentAsString
    contentOfUpdatedTocFile should include("changed title")
    contentOfUpdatedTocFile should include("foo")
    contentOfUpdatedTocFile should include(changedUpdatedAtTime.toString)
  }

}
