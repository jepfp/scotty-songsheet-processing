package ch.scotty.job

import ch.scotty.Db
import ch.scotty.job.json.result.{Failure, PerJobDefinitionResultHolder, Success}
import ch.scotty.job.json.{JobConfiguration, JobDefinitions}

object Job{
  val UNKNOWN_EXCEPTION_IN_JOB_EXECUTION_OCCURRED = "Unknown exception in job execution occurred"
}

trait Job[J <: JobConfiguration] {
  val db: Db
  private val resultBuilder = new PerJobDefinitionResultHolder.Builder()

  final def runIfJobsDefined(jobDefinitions: JobDefinitions): PerJobDefinitionResultHolder = {
    val c = getJobConfigurations(jobDefinitions)
    println(s"\nFinding job configurations for ${getClass.getSimpleName.replace("$", "")}...")
    if (c.isDefined && c.get.nonEmpty) {
      val jobConfigurations = c.get
      println("Found " + jobConfigurations.size + " job configuration(s).")
      jobConfigurations.foreach(executeRunWithTryCatchAndStoreResult)
    }
    resultBuilder.build()
  }

  private def executeRunWithTryCatchAndStoreResult(jobConfig: J) = {
    resultBuilder.addAnEitherResult(
      try {
        run(jobConfig)
      } catch {
        case ex: Exception => Left(Failure(jobConfig.jobId, Job.UNKNOWN_EXCEPTION_IN_JOB_EXECUTION_OCCURRED, Seq(ex)))
      })
  }

  def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[J]]

  def run(jobConfiguration: J): Either[Failure, Success]
}
