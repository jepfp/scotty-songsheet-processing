package ch.scotty.job.json

import ch.scotty.UnitSpec
import java.util.UUID

class JobParserTest extends UnitSpec {
  val fooUid1 = "b46ff506-468f-4096-9f9d-7c17297145ae"
  val fooUid2 = "a46ff506-468f-4096-9f9d-7c17297145ae"

  val singleValidJob =
    s"""
  {
    "jobId" : "${fooUid1}",
    "jobName" : "SingleSongToImageConverterJob",
    "configuration" : {
      "songId" : 5
    }
  }"""

  val jsonWithOneValidJob = s"[${singleValidJob}]"

  val jsonWithTwoValidJobs =
    s"""[
  ${singleValidJob},
  {
    "jobId" : "${fooUid2}",
    "jobName" : "SingleSongToImageConverterJob",
    "configuration" : {
      "songId" : 4
    }
  }
  ]"""

  val jsonWithMissingJobId =
    s"""[
    {
      "jobName" : "SingleSongToImageConverterJob",
        "configuration" : {
      "songId" : 5
      }
    }]"""

  val jsonWithInvalidJobId =
    s"""[
  {
    "jobId" : "foo",
    "jobName" : "SingleSongToImageConverterJob",
    "configuration" : {
      "songId" : 5
    }
  }
  ]"""

  val jsonWithMissingLiedId =
    s"""
    [{
      "jobId" : "${fooUid1}",
      "jobName" : "SingleSongToImageConverterJob",
        "configuration" : {}
    }]"""

  "The JobParser" should "return a Set with one job case object for a valid json" in {
    // arrange
    val testee = new JobParser()
    // act
    val jobs = testee.parseJobJson(jsonWithOneValidJob)
    // assert
    assertResult(1)(jobs.length)
    assertResult(UUID.fromString(fooUid1))(jobs.head.jobId)
    assertResult("SingleSongToImageConverterJob")(jobs.head.jobName)
  }

  it should "return a Set with two job case objects for a valid json" in {
    // arrange
    val testee = new JobParser()
    // act
    val jobs = testee.parseJobJson(jsonWithTwoValidJobs)
    // assert
    assertResult(2)(jobs.length)
    assertResult(UUID.fromString(fooUid1))(jobs.head.jobId)
    assertResult(UUID.fromString(fooUid2))(jobs(1).jobId)
  }

  it should "throw an exception if jobId is missing" in {
    // arrange
    val testee = new JobParser()
    // act
    intercept[JobParsingException](testee.parseJobJson(jsonWithMissingJobId))
  }

  it should "throw an exception if jobId is not a valid UUID" in {
    // arrange
    val testee = new JobParser()
    // act
    intercept[JobParsingException](testee.parseJobJson(jsonWithInvalidJobId))
  }

  it should "throw an exception if liedId is missing" in {
    // arrange
    val testee = new JobParser()
    // act
    intercept[JobParsingException](testee.parseJobJson(jsonWithMissingLiedId))
  }
}