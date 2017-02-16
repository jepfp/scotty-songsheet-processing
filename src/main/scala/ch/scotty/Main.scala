package ch.scotty

import java.nio.file.{Files, Path, Paths}

import ch.scotty.job.JobRunner
import ch.scotty.job.json.result.JobResultWriter
import ch.scotty.job.json.{JobDefinitions, JobParser}

object Main {
  val defaultJobsPath: String = "jobs.json"

  implicit val db = new DefaultDb()

  def main(args: Array[String]): Unit = {
    val jobsPath = determineAndValidateJobsPath(args)
    println(s"Reading job definitions from '$jobsPath'...")
    val jobDefinitions: JobDefinitions = JobParser.parseJobJson(readJsonFile(jobsPath.toString))
    println("Running jobs...")
    val jobRunner = new JobRunner(jobDefinitions)
    val jobResults = jobRunner.runAllJobs()
    JobResultWriter.writeJobResults(createResultFile(jobsPath), jobResults)
    db.db.close()
  }

  private def createResultFile(jobsPath: Path) = {
    val p = Paths.get(jobsPath.getParent.toString, "result_" + jobsPath.getFileName.toString)
    p.toFile
  }

  def determineAndValidateJobsPath(args: Array[String]): Path = {
    val p = if (args.length > 0) Paths.get(args(0)) else Paths.get(defaultJobsPath)
    if (Files.notExists(p)) throw new IllegalArgumentException(s"File with path '$p' does not exist.")
    p
  }

  def readJsonFile(jobsPath: String): String = {
    val source = scala.io.Source.fromFile(jobsPath)
    try source.mkString finally source.close()
  }
}