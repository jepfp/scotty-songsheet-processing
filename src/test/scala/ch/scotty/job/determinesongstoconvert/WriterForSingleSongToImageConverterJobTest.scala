package ch.scotty.job.determinesongstoconvert

import java.io.File
import java.util.UUID

import ch.scotty.IntegrationSpec
import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration
import ch.scotty.job.json.result._

class WriterForSingleSongToImageConverterJobTest extends IntegrationSpec with TestFolder {

  behavior of "WriterForSingleSongToImageConverterJobTest"

  it should "generateAndWriteJobFile write the results to a json as expected" in {
    // arrange
    val uuid1 = UUID.fromString("cc07c7e8-f160-11e6-bc64-92361f002671");
    val uuid2 = UUID.fromString("cc07c7e8-f160-11e6-bc64-92361f002672");
    val uuid3 = UUID.fromString("cc07c7e8-f160-11e6-bc64-92361f002673");
    val jobs = Seq((uuid1, 1L),(uuid2, 2L),(uuid3, 3L)).map(t =>  SingleSongToImageConverterJobConfiguration(t._1, t._2))
    val outputFile = new File(testFolder, "actualJob.json")
    val testee = new WriterForSingleSongToImageConverterJob()
    // act
    testee.generateAndWriteJobFile(outputFile, jobs)
    // assert
    val expectedResult = readResourceFile("expectedJobInputFile.json")
    val source = scala.io.Source.fromFile(outputFile)
    val actualResult = try source.mkString finally source.close()
    assertResult(expectedResult)(actualResult)
  }

  def readResourceFile(filename: String): String = {
    val stream = getClass.getResourceAsStream(filename)
    val source = scala.io.Source.fromInputStream(stream)
    try source.mkString finally source.close()
  }

}
