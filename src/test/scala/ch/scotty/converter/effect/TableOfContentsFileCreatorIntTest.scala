package ch.scotty.converter.effect

import java.io.{File => JFile}
import java.time.LocalDateTime

import better.files._
import ch.scotty.converter.ConversionResults.Success
import ch.scotty.converter.{LiedWithData, Songnumber}
import ch.scotty.{TestFolder, _}
import com.typesafe.config.ConfigFactory
import org.scalatest.Matchers

import scala.collection.JavaConversions._

class TableOfContentsFileCreatorIntTest extends IntegrationSpec with TestFolder with Matchers {

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
    val result = testee.createFile(createLiedWithNumber(), createSongnumber, 3, "2745913656", "20190909121340")
    //assert
    assertExpectedAndActualJsonIsTheSame(generateJsonFilename())
    assertResult(Success(None))(result)
  }

  def assertExpectedAndActualJsonIsTheSame(filename: String) = {
    val expectedFile: File = File(getClass.getResource(filename).toURI)
    val actualFile: File = new JFile(testFolder.getPath, filename).toScala
    expectedFile.contentAsString shouldBe actualFile.contentAsString
  }

  private def createTestee = {
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString))
    val exportPathResolverAndCreator = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap))
    new TableOfContentsFileCreator(exportPathResolverAndCreator)
  }

  private def generateJsonFilename() = {
    s"${LIED_ID}.json"
  }

  private def createSongnumber = {
    Seq(Songnumber(LIED_ID, LIEDERBUCH_ID, MNEMONIC, LIEDERBUCH, LIED_NR))
  }

  private def createLiedWithNumber() = {
    LiedWithData(LIED_ID, "foo", Some("C"), LocalDateTime.MIN, updatedAt, null)
  }

}
