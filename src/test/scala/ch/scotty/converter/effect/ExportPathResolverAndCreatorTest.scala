package ch.scotty.converter.effect

import ch.scotty.converter.SourceSystem
import ch.scotty.{TestFolder, UnitSpec}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File
import java.nio.file.{Files, Paths}

class ExportPathResolverAndCreatorTest extends UnitSpec with TestFolder {

  val config: Config = ConfigFactory.load("ExportPathResolverAndCreatorTest.conf")

  "resolve" should "contain configured path in string" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(config)
    // act
    val actualString = testee.resolve(SourceSystem.Scotty, "foo")
    // assert

    actualString should include(s"data-test-dir${File.separator}scotty")
  }

  it should "return an absolute file path" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(config)
    // act
    val actualString = testee.resolve(SourceSystem.Scotty, "./")
    val path = Paths.get(actualString)
    // assert
     path.isAbsolute should be(true)
  }

  it should "create the directory structure if it does not yet exist" in {
    // arrange
    import scala.jdk.CollectionConverters._
    val configMap: Map[String, String] = Map("converter.exportBaseDir" -> (testFolder.getPath.toString + "/dir1/dir2/dir3"))
    val testee = new ExportPathResolverAndCreator(ConfigFactory.parseMap(configMap.asJava))
    // act
    val actualString = testee.resolve(SourceSystem.Scotty, "foo")
    // assert
    actualString should include("dir3")
    val pathWithoutFileName = actualString.replace("foo", "")
    Files.exists(Paths.get(pathWithoutFileName)) should be(true)
  }
}

