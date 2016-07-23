package ch.scotty.job

import ch.scotty.job.json.JobParser

object JobRunner {

  val jobsPath : String = "jobs.json"

  def runAllJobs() : Unit = {
//    val allJobs = readJobs()

  }

//  def readJobs(): Seq[Job] = {
//    val source = scala.io.Source.fromFile("jobs.json")
//    val content = try source.mkString finally source.close()
//    val parser = new JobParser()
//    parser.parseJobJson(content)
//  }
}
