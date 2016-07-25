package ch.scotty.job

import ch.scotty.job.json.{JobConfiguration, JobDefinitions, JobParser}

object JobRunner {
  def runAllJobs(jobDefinitions: JobDefinitions): Unit = {
    AllSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
    SingleSongToImageConverterJob.runIfJobsDefined(jobDefinitions)
  }

}
