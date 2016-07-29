package ch.scotty.job

import ch.scotty.Stopwatch
import ch.scotty.converter.LiedSourcePdfFileFinder
import ch.scotty.job.json.{JobConfiguration, JobDefinitions, JobParser}

object JobRunner {

  val singleSongToImageConverterJob = new SingleSongToImageConverterJob()
  def runAllJobs(jobDefinitions: JobDefinitions): Unit = {


    val sw = Stopwatch.time("All Jobs") {
      AllSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
      singleSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
    }
    println
    println(s"All jobs have been executed. Duration: $sw")
  }

}
