package ch.scotty.converter

import java.io.{File => JFile}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import better.files._
import ch.scotty._
import ch.scotty.converter.ConversionResults.Success
import ch.scotty.job.json.result.TestFolder
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConversions._

class TableOfContentsFileCreatorIntTest extends IntegrationSpec with TestFolder {

  private val LIED_ID = 1
  private val LIEDERBUCH_ID = 2
  private val MNEMONIC = "mnemonic"
  private val LIEDERBUCH = "liederbuch"
  private val LIED_NR = "liednr"
  private val updatedAt = LocalDateTime.of(2014, 5, 29, 7, 22)


  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  "createFile" should s"convert into the expected json and return Success" in {
    //arrange
    val testee = createTestee
    //act
    val result = testee.createFile(createLiedWithNumber, createSongnumber)
    //assert
    assertExpectedAndActualJsonIsTheSame(generateJsonFilename())
    assertResult(Success())(result)
  }

  def assertExpectedAndActualPictureIsTheSame(filename: String) = {
    val expectedFile: File = File(getClass.getResource(filename).toURI)
    val actualFile: File = new JFile(testFolder.getPath, filename).toScala
    expectedFile === actualFile
    assert(expectedFile.isSameContentAs(actualFile))
  }

  def assertExpectedAndActualJsonIsTheSame(filename: String) = {
    val expectedFile: File = File(getClass.getResource(filename).toURI)
    val actualFile: File = new JFile(testFolder.getPath, filename).toScala
    expectedFile === actualFile
    assert(expectedFile.isSameContentAs(actualFile))
  }

  private def createTestee = {
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString))
    val exportPathResolverAndCreator = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap))
    new TableOfContentsFileCreator(exportPathResolverAndCreator)
  }

  private def generateJsonFilename() = {
    s"${LIED_ID}-${updatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}.json"
  }

  private def createSongnumber = {
    Seq(Songnumber(LIED_ID, LIEDERBUCH_ID, MNEMONIC, LIEDERBUCH, LIED_NR))
  }

  private def createLiedWithNumber() = {
    LiedWithData(LIED_ID, "foo", Some("C"), LocalDateTime.MIN, updatedAt, null)
  }

}
