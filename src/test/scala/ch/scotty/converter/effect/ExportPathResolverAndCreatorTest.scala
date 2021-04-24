package ch.scotty.converter.effect

import ch.scotty.converter.{SongsheetConfig, SourceSystem}
import ch.scotty.{TestFolder, UnitSpec}

import java.io.File
import java.nio.file.{Files, Paths}

class ExportPathResolverAndCreatorTest extends UnitSpec with TestFolder {

  val songsheetConfig = SongsheetConfig.get(Map("scotty-songsheet-processing.exportBaseDir" -> "./data-test-dir/"))

  "resolve" should "contain configured path in string" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(songsheetConfig)
    // act
    val actualString = testee.resolve(SourceSystem.Scotty, "foo")
    // assert

    actualString should include(s"data-test-dir${File.separator}scotty")
  }

  it should "return an absolute file path" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(songsheetConfig)
    // act
    val actualString = testee.resolve(SourceSystem.Scotty, "./")
    val path = Paths.get(actualString)
    // assert
    path.isAbsolute should be(true)
  }

  it should "create the directory structure if it does not yet exist" in {
    // arrange
    val testee = new ExportPathResolverAndCreator(SongsheetConfig.get(Map("scotty-songsheet-processing.exportBaseDir" -> testFolder.getPath)))
    // act
    val actualString = testee.resolve(SourceSystem.Scotty, "foo")
    // assert
    actualString should include("dir3")
    val pathWithoutFileName = actualString.replace("foo", "")
    Files.exists(Paths.get(pathWithoutFileName)) should be(true)
  }
}

