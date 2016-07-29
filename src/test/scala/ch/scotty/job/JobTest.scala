package ch.scotty.job

import java.util.UUID

import ch.scotty.{Db, UnitSpec}
import ch.scotty.job.json.{JobConfiguration, JobDefinitions}

class JobTest extends UnitSpec {

  private val aJobConfig = ConcreteJobConfiguration(UUID.fromString("b46ff506-468f-4096-9f9d-7c17297145ae"), "foofoo")
  private val fooJobDef = JobDefinitions(None, None) // not relevant for this tests!

  private def createConfigSeqOf(configs: ConcreteJobConfiguration*): Option[Seq[ConcreteJobConfiguration]] = Option(configs)

  "runIfJobsDefined" should "execute run if there is one given jobConfiguration" in {
    // arrange
    var hasRunBeenCalledCorrectly = false
    object concreteJob extends Job[ConcreteJobConfiguration] {
      val db = stub[Db]

      override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig)

      override def run(jobConfiguration: ConcreteJobConfiguration): Unit = {
        if (jobConfiguration.equals(aJobConfig)) {
          hasRunBeenCalledCorrectly = true
        }
      }
    }
    // act
    concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(true)(hasRunBeenCalledCorrectly)
  }

  it should "never execute run if getJobConfigurations returns Some with an empty list of jobs" in {
    // arrange
    var hasRunBeenCalled = false
    object concreteJob extends Job[ConcreteJobConfiguration] {
      val db = stub[Db]

      override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf()

      override def run(jobConfiguration: ConcreteJobConfiguration): Unit = {
          hasRunBeenCalled = true
      }
    }
    // act
    concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(expected = false)(hasRunBeenCalled)
  }

  it should "never execute run if getJobConfigurations returns None" in {
    // arrange
    var hasRunBeenCalled = false
    object concreteJob extends Job[ConcreteJobConfiguration] {
      val db = stub[Db]

      override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[ConcreteJobConfiguration]] = None

      override def run(jobConfiguration: ConcreteJobConfiguration): Unit = {
        hasRunBeenCalled = true
      }
    }
    // act
    concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(expected = false)(hasRunBeenCalled)
  }

  it should "execute run 2 times if there are two given jobConfigurations" in {
    // arrange
    var runCallCounter = 0
    object concreteJob extends Job[ConcreteJobConfiguration] {
      val db = stub[Db]

      override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[ConcreteJobConfiguration]] = createConfigSeqOf(aJobConfig, aJobConfig)

      override def run(jobConfiguration: ConcreteJobConfiguration): Unit = {
        runCallCounter += 1
      }
    }
    // act
    concreteJob.runIfJobsDefined(fooJobDef)
    // assert
    assertResult(2)(runCallCounter)
  }

  private case class ConcreteJobConfiguration(jobId: UUID, importantTestString: String) extends JobConfiguration

}
