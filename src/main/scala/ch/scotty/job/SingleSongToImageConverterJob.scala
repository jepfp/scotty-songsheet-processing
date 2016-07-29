package ch.scotty.job
import ch.scotty.converter.{SongnumberFinder, _}
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}

class SingleSongToImageConverterJob extends Job[SingleSongToImageConverterJobConfiguration]{
  protected lazy val liedSourcePdfFileFinder = new LiedSourcePdfFileFinder()
  protected lazy val songnumberFinder = new SongnumberFinder()
  protected lazy val liedPdfToImageConverter = new LiedPdfToImageConverter()


  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[SingleSongToImageConverterJobConfiguration]] = jobDefinitions.singleSongToImageConverterJob

  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Unit = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println(s"Converting song with id ${jobConfiguration.songId}...")

    val songId = jobConfiguration.songId
    val liedData = liedSourcePdfFileFinder.findFile(songId)
    val songnumbers = songnumberFinder.findSongnumbers(songId)
    liedPdfToImageConverter.convertPdfBlobToImage(liedData, songnumbers)
  }
}
