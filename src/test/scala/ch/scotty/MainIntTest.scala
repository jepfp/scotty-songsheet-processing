package ch.scotty

import java.io.File
import java.nio.file.{Files, Paths}

import ch.scotty.job.json.result.TestFolder

class MainIntTest extends IntegrationSpec with TestFolder {

  private val jobsResourceFileName = "mainTest.json"

  //http://stackoverflow.com/questions/37902008/calling-curried-functions-in-scala
  //_ tells the compiler explicitly that I want a function type
  //("missing argument list for method averageDamp in object Foo Unapplied methods are only converted to functions when a function type is expected.")
  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  "main" should "when an arg is set read the job definition from the given arg and perform a working round trip with the expected result file" in {
    //arrange
    val jobsInputStream = getClass.getResourceAsStream(jobsResourceFileName)
    val jobsFilePathInTempFolder = Paths.get(testFolder.getPath, jobsResourceFileName)
    Files.copy(jobsInputStream, jobsFilePathInTempFolder)
    //act
    Main.main(Array[String](jobsFilePathInTempFolder.toString))
    //assert
    assertContentEquals("mainTestExpectedResult.json", new File(testFolder.getPath, "result_" + jobsResourceFileName))
  }
}
