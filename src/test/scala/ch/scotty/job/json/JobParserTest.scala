package ch.scotty.job.json

import ch.scotty.UnitSpec
import java.util.UUID

class JobParserTest extends UnitSpec {
  val fooUuid1 = "b46ff506-468f-4096-9f9d-7c17297145ae"
  val fooUuid2 = "a46ff506-468f-4096-9f9d-7c17297145ae"

  "The JobParser" should "return a Set with one SingleSongToImageConverterJobConfiguration case object for a valid json" in {
    // arrange
    // act
    val jobs = JobParser.parseJobJson(readJsonFile("parse_1singleSongToImageConverterJob_1jobCreated.json"))
    // assert
    assertResult(true)(jobs.singleSongToImageConverterJob.isDefined)
    assertResult(UUID.fromString(fooUuid1))(jobs.singleSongToImageConverterJob.get.head.jobId)
  }

  def readJsonFile(filename: String): String = {
    val stream = getClass.getResourceAsStream(filename)
    val source = scala.io.Source.fromInputStream(stream)
    try source.mkString finally source.close()
  }

  it should "return a Set with two SingleSongToImageConverterJobConfiguration case objects for a valid json" in {
    // arrange
    // act
    val jobs = JobParser.parseJobJson(readJsonFile("parse_2singleSongToImageConverterJobs_2jobsCreated.json"))
    // assert
    assertResult(2)(jobs.singleSongToImageConverterJob.get.size)
    assertResult(UUID.fromString(fooUuid1))(jobs.singleSongToImageConverterJob.get.head.jobId)
    assertResult(UUID.fromString(fooUuid2))(jobs.singleSongToImageConverterJob.get(1).jobId)
  }

  it should "throw an exception if jobId is not a valid UUID" in {
    // arrange
    val fileContent = readJsonFile("parse_1singleSongToImageConverterJobWithInvalidUuid_exception.json")
    // act
    val message = intercept[JobParsingException](JobParser.parseJobJson(fileContent)).getMessage
    message should include("error.expected.uuid")
  }

  it should "return a Set with one AllSongToImageConverterJobConfiguration case object for a valid json" in {
    // arrange
    // act
    val jobs = JobParser.parseJobJson(readJsonFile("parse_1allSongToImageConverterJob_1jobCreated.json"))
    // assert
    assertResult(true)(jobs.allSongToImageConverterJob.isDefined)
    assertResult(UUID.fromString(fooUuid1))(jobs.allSongToImageConverterJob.get.head.jobId)
  }

  it should "throw an exception if songId is missing in a SingleSongToImageConverterJobConfiguration" in {
    // arrange
    val fileContent = readJsonFile("parse_1singleSongToImageConverterJobWithoutSongId_exception.json")
    // act
    intercept[JobParsingException](JobParser.parseJobJson(fileContent)) should have message
      "Error while parsing jobs: {\"obj.singleSongToImageConverterJob[0].songId\":[{\"msg\":[\"error.path.missing\"],\"args\":[]}]}"
  }

  it should "throw an exception if there are no job definitions" in {
    // arrange
    val fileContent = readJsonFile("parse_noJobDefinitions_exception.json")
    // act
    intercept[JobParsingException](JobParser.parseJobJson(fileContent)) should have message
      "No job definitions found."
  }
}