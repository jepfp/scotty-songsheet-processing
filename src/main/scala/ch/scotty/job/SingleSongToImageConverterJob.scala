package ch.scotty.job
import ch.scotty.converter._
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}

object SingleSongToImageConverterJob extends Job[SingleSongToImageConverterJobConfiguration]{
  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[SingleSongToImageConverterJobConfiguration]] = jobDefinitions.singleSongToImageConverterJob

  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Unit = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println(s"Converting song with id ${jobConfiguration.songId}...")

    val songId = jobConfiguration.songId
    val liedData = LiedSourcePdfFileFinder.findFile(songId)
    val songnumbers = SongnumberFinder.findSongnumbers(songId)
    LiedPdfToImageConverter.convertPdfBlobToImage(liedData, songnumbers)
  }
}
