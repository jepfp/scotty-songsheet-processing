package ch.scotty.job

import ch.scotty.{Db, Stopwatch}
import ch.scotty.job.json.JobDefinitions

class JobRunner(implicit val db: Db) {

  val singleSongToImageConverterJob = new SingleSongToImageConverterJob()
  val allSongToImageConverterJob = new AllSongToImageConverterJob()

  def runAllJobs(jobDefinitions: JobDefinitions): Unit = {
    val sw = Stopwatch.time("All Jobs") {
      allSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
      singleSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
    }
    println
    println(s"All jobs have been executed. Duration: $sw")
  }

}
