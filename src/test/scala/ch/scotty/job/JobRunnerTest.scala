package ch.scotty.job

import java.util.UUID

import ch.scotty.job.determinesongstoconvert.DetermineSongsToConvertJob
import ch.scotty.job.json.result.{Failure, PerJobDefinitionResultHolder, Success}
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}
import ch.scotty.{Db, UnitSpec}

class JobRunnerTest extends UnitSpec {
  implicit val db = stub[Db]
  val uuid: UUID = UUID.fromString("d3c00b29-7a77-4d1d-8996-fc0816f19acf")
  val jobDefinitions: JobDefinitions = JobDefinitions(Some(Seq(SingleSongToImageConverterJobConfiguration(uuid, 5))), None, None)
  private val singleSongToImageConverterJobStub = stub[SingleSongToImageConverterJob]
  private val determineSongsToConvertJob = stub[DetermineSongsToConvertJob]
  trainJobStub()

  "runAllJobs" should "return one success job result for a defined job" in {
    //arrange
    val testee = new JobRunner(jobDefinitions, singleSongToImageConverterJobStub, allSongToImageConverterJobStub, determineSongsToConvertJob)
    //act
    val jobResults = testee.runAllJobs()
    //assert
    assertResult(Success(uuid))(jobResults.get(singleSongToImageConverterJobStub.getClass.getSimpleName).get.success.head)
  }
  private val allSongToImageConverterJobStub = stub[AllSongToImageConverterJob]

  private def trainJobStub() = {
    val resultBuilder = new PerJobDefinitionResultHolder.Builder
    resultBuilder.addAnEitherResult(Right(Success(uuid)))
    (singleSongToImageConverterJobStub.runIfJobsDefined(_)).when(jobDefinitions).returns(resultBuilder.build)
  }

  "runAllJobs" should "create a map entry with a key named after the class of the executed job" in {
    //arrange
    val concreteJob = new ConcreteJobWithCamelCaseNaming()
    val testee = new JobRunner(jobDefinitions, concreteJob, allSongToImageConverterJobStub, determineSongsToConvertJob)
    //act
    val jobResults = testee.runAllJobs()
    //assert
    val className = classOf[ConcreteJobWithCamelCaseNaming].getSimpleName
    println(className)
    assertResult(Success(uuid))(jobResults.get(className).get.success.head)
  }

  private class ConcreteJobWithCamelCaseNaming extends SingleSongToImageConverterJob {
    override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Either[Failure, Success] = {
      // just pretend that it did whatever
      return Right(Success(jobConfiguration.jobId))
    }
  }


}
