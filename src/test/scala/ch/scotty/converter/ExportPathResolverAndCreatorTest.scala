package ch.scotty.converter

import java.nio.file.{Files, Paths}

import ch.scotty.{TestFolder, UnitSpec}
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConversions._

class ExportPathResolverAndCreatorTest extends UnitSpec with TestFolder {

  val config: Config = ConfigFactory.load("ExportPathResolverAndCreatorTest.conf")

  "resolve" should "contain configured path in string" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(config)
    // act
    val actualString = testee.resolve("foo")
    // assert
    actualString should include("data-test-dir")
  }

  it should "return an absolute file path" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(config)
    // act
    val actualString = testee.resolve("./")
    val path = Paths.get(actualString)
    // assert
     path.isAbsolute should be(true)
  }

  it should "create the directory structure if it does not yet exist" in {
    // arrange
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString + "/dir1/dir2/dir3"))
    val testee = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap))
    // act
    val actualString = testee.resolve("foo")
    // assert
    actualString should include("dir3")
    val pathWithoutFileName = actualString.replace("foo", "")
    Files.exists(Paths.get(pathWithoutFileName)) should be(true)
  }
}

