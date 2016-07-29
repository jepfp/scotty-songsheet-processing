package ch.scotty.job

import ch.scotty.Stopwatch
import ch.scotty.job.json.{JobConfiguration, JobDefinitions, JobParser}

object JobRunner {
  def runAllJobs(jobDefinitions: JobDefinitions): Unit = {
    val sw = Stopwatch.time("All Jobs") {
      AllSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
      SingleSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
    }
    println
    println(s"All jobs have been executed. Duration: $sw")
  }

}
