package ch.scotty.job

import ch.scotty.Db
import ch.scotty.job.json.result.{Failure, PerJobDefinitionResultHolder, Success}
import ch.scotty.job.json.{JobConfiguration, JobDefinitions}

trait Job[J <: JobConfiguration] {
  val db: Db
  private val resultBuilder = new PerJobDefinitionResultHolder.Builder()

  private def determineJobDefinition(): String ={
    getClass.getSimpleName.replace("$", "")
  }

  def runIfJobsDefined(jobDefinitions: JobDefinitions): PerJobDefinitionResultHolder = {
    val c = getJobConfigurations(jobDefinitions)
    println(s"\nFinding job configurations for ${determineJobDefinition}...")
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
        case ex: Exception => {
          val m = s"The exception '${ex.getClass.getSimpleName}' occured for job '${jobConfig.jobId}' in definition '${determineJobDefinition}'."
          Console.err.println(m)
          Left(Failure(jobConfig.jobId, m, Seq(ex)))
        }
      })
  }

  def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[J]]

  def run(jobConfiguration: J): Either[Failure, Success]
}
