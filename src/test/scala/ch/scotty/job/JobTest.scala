package ch.scotty.job

import java.util.UUID

import ch.scotty.converter.ConverterException
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.job.json.{JobConfiguration, JobDefinitions}
import ch.scotty.{Db, UnitSpec}

class JobTest extends UnitSpec {

  private val aJobConfig = ConcreteJobConfiguration(UUID.fromString("b46ff506-468f-4096-9f9d-7c17297145ae"), "foofoo")
  private val anotherJobConfig: ConcreteJobConfiguration = aJobConfig.copy(jobId = UUID.fromString("b46ff506-468f-4096-9f9d-7c17297145ab"))
  private val fooJobDef = JobDefinitions(None, None) // None because not relevant for this tests!

  def runImplementationImitatingASuccessfulJobExecution(jobConfiguration: ConcreteJobConfiguration): Either[Failure, Success] = {
    // job returns Success object --> All jobs must do that if they succeed
    return Right(Success(jobConfiguration.jobId))
  }

  "runIfJobsDefined" should "execute run if there is one given jobConfiguration" in {
    // arrange
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig)
    def runImplementation(jobConfiguration: ConcreteJobConfiguration): Either[Failure, Success] = {
      assertResult(aJobConfig)(jobConfiguration)
      return Right(Success(jobConfiguration.jobId))
    }
    val concreteJob = new ConcreteJob(givenJobConfiguration, runImplementation)
    // act & assert
    concreteJob.runIfJobsDefined(fooJobDef)
  }

  private def createConfigSeqOf(configs: ConcreteJobConfiguration*): Option[Seq[ConcreteJobConfiguration]] = Option(configs)

  private class ConcreteJob(jobConfigToReturn: Option[Seq[ConcreteJobConfiguration]], runImplementation: (ConcreteJobConfiguration) => Either[Failure, Success]) extends Job[ConcreteJobConfiguration] {
    val db = stub[Db]

    override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[ConcreteJobConfiguration]] = jobConfigToReturn

    override def run(jobConfiguration: ConcreteJobConfiguration): Either[Failure, Success] = runImplementation(jobConfiguration)
  }

  it should "return / forward a Success object for each job (if (and this should always be true) the run implementation of the job returns the Success object)" in {
    // arrange
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig, anotherJobConfig)
    val concreteJob = new ConcreteJob(givenJobConfiguration, runImplementationImitatingASuccessfulJobExecution)
    // act
    val result = concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(Seq(Success(aJobConfig.jobId), Success(anotherJobConfig.jobId)))(result.success)
  }

  it should "return one Success object an empty failure sequence if there was one job executed successfully" in {
    // arrange
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig)
    val concreteJob = new ConcreteJob(givenJobConfiguration, runImplementationImitatingASuccessfulJobExecution)
    // act
    val result = concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(Seq(Success(aJobConfig.jobId)))(result.success)
    assertResult(Seq.empty[Failure])(result.failure)
  }

  it should "return one Failure object with the correct error message and an empty success sequence if there was one job throwing an exception" in {
    // arrange
    val exceptionToBeThrown: IllegalStateException = new IllegalStateException("Something strange went wrong")
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig)
    val concreteJob = new ConcreteJob(givenJobConfiguration, c =>
      throw exceptionToBeThrown
    )
    // act
    val result = concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    val expectedFailure = Failure(aJobConfig.jobId, "The exception 'IllegalStateException' occured for job 'b46ff506-468f-4096-9f9d-7c17297145ae' in definition 'ConcreteJob'.", Seq(exceptionToBeThrown))
    assertResult(Seq(expectedFailure))(result.failure)
    assertResult(Seq.empty[Success])(result.success)
  }

  it should "forwad a failure if a specific run implementation created it" in {
    // arrange
    val exception1ToBeThrown = new ConverterException("No valid PDF.")
    val exception2ToBeThrown = new ConverterException("Unknown conversion exception")
    val aFailure = Failure(aJobConfig.jobId, "Conversion of songs 1, 4 and 9 failed. See exception list for details.", Seq(exception1ToBeThrown, exception2ToBeThrown))
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig)
    val concreteJob = new ConcreteJob(givenJobConfiguration, c => Left(aFailure))
    // act
    val result = concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(Seq(aFailure))(result.failure)
    assertResult(Seq.empty[Success])(result.success)
  }

  it should "never execute run if getJobConfigurations returns Some with an empty list of jobs" in {
    // arrange
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf()
    def runImplementation(jobConfiguration: ConcreteJobConfiguration): Either[Failure, Success] = {
      fail("This must not be called.")
      return Right(Success(jobConfiguration.jobId))
    }
    val concreteJob = new ConcreteJob(givenJobConfiguration, runImplementation)
    // act & assert
    concreteJob.runIfJobsDefined(fooJobDef)
  }

  it should "never execute run if getJobConfigurations returns None" in {
    // arrange
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = None
    def runImplementation(jobConfiguration: ConcreteJobConfiguration): Either[Failure, Success] = {
      fail("This must not be called.")
      return Right(Success(jobConfiguration.jobId))
    }
    val concreteJob = new ConcreteJob(givenJobConfiguration, runImplementation)
    // act & assert
    concreteJob.runIfJobsDefined(fooJobDef)
  }

  it should "execute run 2 times if there are two given jobConfigurations" in {
    // arrange
    var runCallCounter = 0
    val givenJobConfiguration: Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig, aJobConfig)
    def runImplementation(jobConfiguration: ConcreteJobConfiguration): Either[Failure, Success] = {
      runCallCounter += 1
      return Right(Success(jobConfiguration.jobId))
    }
    val concreteJob = new ConcreteJob(givenJobConfiguration, runImplementation)
    // act
    concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(2)(runCallCounter)
  }

  private case class ConcreteJobConfiguration(jobId: UUID, importantTestString: String) extends JobConfiguration

}
