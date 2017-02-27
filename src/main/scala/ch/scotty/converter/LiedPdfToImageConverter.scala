package ch.scotty.converter

import net.java.truecommons.io.Loan._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

private class LiedPdfToImageConverter(exportPathResolverAndCreator : ExportPathResolverAndCreator) {

  def this(){
    this(new ExportPathResolverAndCreator())
  }

  def convertPdfBlobToImage(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {
    try {
      val binaryStream = liedWithData.data.getBinaryStream
      loan(PDDocument.load(binaryStream)).to { doc =>
        val renderer = new PDFRenderer(doc)
        val listOfImage = for (i <- 0 until doc.getNumberOfPages) yield {
          println(s"Exporting '${liedWithData.titel}' page " + (i + 1))
          val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
          val filename = FilenameBuilder.build(liedWithData, songnumbers, i)
          val path = exportPathResolverAndCreator.resolve(filename)
          ImageIOUtil.writeImage(bim, path, 0)
        }
      }
    } catch {
      case e: Exception =>
        System.err.println("Error while exporting " + liedWithData + ". Song is skipped. Error: " + e)
    }
  }

}