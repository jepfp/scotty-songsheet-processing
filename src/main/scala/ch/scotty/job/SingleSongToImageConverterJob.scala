package ch.scotty.job
import java.nio.file.Paths

import ch.scotty.converter.{LiedSourcePdfFileFinder, LiedWithData, Songnumber, SongnumberFinder}
import ch.scotty.job.json.{JobDefinitions, SingleSongToImageConverterJobConfiguration}
import net.java.truecommons.io.Loan._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

object SingleSongToImageConverterJob extends Job[SingleSongToImageConverterJobConfiguration]{
  override def getJobConfigurations(jobDefinitions: JobDefinitions): Option[Seq[SingleSongToImageConverterJobConfiguration]] = jobDefinitions.singleSongToImageConverterJob

  override def run(jobConfiguration: SingleSongToImageConverterJobConfiguration): Unit = {
    println(s"Executing job '${jobConfiguration.jobId}'")
    println(s"Converting song with id ${jobConfiguration.songId}...")

    val songId = jobConfiguration.songId
    val liedData = LiedSourcePdfFileFinder.findFile(songId)
    val songnumbers = SongnumberFinder.findSongnumbers(songId);
    convertPdfBlobToImage(liedData, songnumbers)
  }

  private def convertPdfBlobToImage(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {
    try {
      val binaryStream = liedWithData.data.getBinaryStream
      loan(PDDocument.load(binaryStream)).to { doc =>
        //val doc = PDDocument.load(binaryStream);
        val renderer = new PDFRenderer(doc)
        val listOfImage = for (i <- 0 until doc.getNumberOfPages()) yield {
          println(s"Exporting '${liedWithData.titel}' page " + (i + 1))
          val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
          val filename = buildFilename(liedWithData, songnumbers, i);
          val path = Paths.get("data", filename).toString()
          ImageIOUtil.writeImage(bim, path, 0);
        }
      }
    } catch {
      case e: Exception => {
        System.err.println("Error while exporting " + liedWithData + ". Song is skipped. Error: " + e)

      }
    }
  }

  private def buildFilename(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], sheetnumber: Integer): String = {
    val titelWithOnlyAllowedCharacters = liedWithData.titel.replaceAll("[^a-zA-Z0-9äöüÄÖÜ .]", "")
    val songnumberString = songnumbers.map { x => x.mnemonic + x.liednr }.mkString("_")

    val filename = liedWithData.liedId + "-" + sheetnumber + "-" + songnumberString + "-" + titelWithOnlyAllowedCharacters + "-" + ".png"
    filename
  }
}
