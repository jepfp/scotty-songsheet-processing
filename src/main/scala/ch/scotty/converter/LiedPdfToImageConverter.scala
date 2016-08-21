package ch.scotty.converter

import java.nio.file.Paths

import net.java.truecommons.io.Loan._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

private class LiedPdfToImageConverter {

  def convertPdfBlobToImage(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {
    try {
      val binaryStream = liedWithData.data.getBinaryStream
      loan(PDDocument.load(binaryStream)).to { doc =>
        //val doc = PDDocument.load(binaryStream);
        val renderer = new PDFRenderer(doc)
        val listOfImage = for (i <- 0 until doc.getNumberOfPages) yield {
          println(s"Exporting '${liedWithData.titel}' page " + (i + 1))
          val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
          val filename = FilenameBuilder.build(liedWithData, songnumbers, i)
          val path = Paths.get("data", filename).toString
          ImageIOUtil.writeImage(bim, path, 0)
        }
      }
    } catch {
      case e: Exception =>
        System.err.println("Error while exporting " + liedWithData + ". Song is skipped. Error: " + e)
    }
  }

}