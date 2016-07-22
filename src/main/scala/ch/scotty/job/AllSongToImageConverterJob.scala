package ch.scotty.job

import ch.scotty.job.json.{AllSongToImageConverterJobConfiguration, SingleSongToImageConverterJobConfiguration}

/**
  * Created by Philipp on 12.07.2016.
  */
object AllSongToImageConverterJob extends Job[AllSongToImageConverterJobConfiguration]{
  override def run(jobConfiguration: AllSongToImageConverterJobConfiguration): Unit = {
    println("Going to export all songs")
  }
}
