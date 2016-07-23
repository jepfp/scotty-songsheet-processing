package ch.scotty.job

import ch.scotty.job.json.{AllSongToImageConverterJobConfiguration, SingleSongToImageConverterJobConfiguration}

object AllSongToImageConverterJob extends Job[AllSongToImageConverterJobConfiguration]{
  override def run(jobConfiguration: AllSongToImageConverterJobConfiguration): Unit = {
    println("Going to export all songs")
  }
}
