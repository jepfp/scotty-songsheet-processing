package ch.scotty.converter

import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversion, Success}
import net.java.truecommons.io.Loan._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

private class LiedPdfToImageConverter(exportPathResolverAndCreator: ExportPathResolverAndCreator) {

  def this() {
    this(new ExportPathResolverAndCreator())
  }

  def convertPdfBlobToImage(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]): ConversionResult = {
    try {
      loadPdfConvertAndSaveAllPages(liedWithData, songnumbers)
    } catch {
      case e: Exception =>
        FailedConversion("Error while exporting " + liedWithData + ". Song is skipped. Error: " + e, e)
    }
  }

  private def loadPdfConvertAndSaveAllPages(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {
    val binaryStream = liedWithData.data.getBinaryStream
    loan(PDDocument.load(binaryStream)).to { doc =>
      val renderer = new PDFRenderer(doc)
      for (i <- 0 until doc.getNumberOfPages) yield {
        println(s"Exporting '${liedWithData.titel}' page " + (i + 1))
        convertAndSave(renderer, i, generatePathString(liedWithData, songnumbers, i))
      }
      Success()
    }
  }

  private def convertAndSave(renderer: PDFRenderer, pageNumber: Int, path: String) = {
    val bim = renderer.renderImageWithDPI(pageNumber, 90, ImageType.RGB)
    ImageIOUtil.writeImage(bim, path, 0)
  }

  private def generatePathString(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], i: Int) = {
    val filename = FilenameBuilder.build(liedWithData, songnumbers, i)
    val path = exportPathResolverAndCreator.resolve(filename)
    path
  }
}



