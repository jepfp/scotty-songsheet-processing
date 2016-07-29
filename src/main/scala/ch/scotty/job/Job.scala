package ch.scotty.job

import ch.scotty.Db
import ch.scotty.job.json.JobDefinitions

trait Job[J] {
  val db : Db

  final def runIfJobsDefined(jobDefinitions: JobDefinitions): Unit = {
    val c = getJobConfigurations(jobDefinitions)
    println
    println(s"Finding job configurations for ${getClass.getSimpleName.replace("$", "")}...")
    if (c.isDefined && c.get.nonEmpty) {
      val jobConfigurations = c.get
      println("Found " + jobConfigurations.size + " job configuration(s).")
      jobConfigurations.foreach(run(_))
    }
  }

  def getJobConfigurations(jobDefinitions: JobDefinitions) : Option[Seq[J]]

  def run(jobConfiguration : J) : Unit
}
