package ch.scotty.job
import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration

/**
  * Created by Philipp on 12.07.2016.
  */
object SingleSongToImageConverterJob extends Job[SingleSongToImageConverterJobConfiguration]{
  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Unit = {
    println("jobConfiguration.lied = " + jobConfiguration.songId)
  }
}
