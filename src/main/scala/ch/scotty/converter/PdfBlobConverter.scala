package ch.scotty.converter

import java.awt.image.BufferedImage

import ch.scotty.converter.effect._
import com.typesafe.scalalogging.Logger
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

import scala.util.Try

private class PdfBlobConverter(override val exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) extends BlobConverter {

  val logger = Logger(classOf[PdfBlobConverter])

  override def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String): Try[Int] = {
    Try {
      val doc: PDDocument = PDDocument.load(data)
      using(doc) { doc =>
        val renderer = new PDFRenderer(doc)
        val amountOfPages = doc.getNumberOfPages
        for (i <- 0 until amountOfPages) yield {
          logger.debug("Exporting songId={}, pageIndex={} of {}", songId, i, amountOfPages)
          val bim: BufferedImage = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
          ImageIOUtil.writeImage(bim, generatePathString(sourceSystem, songId, i, dataChecksum), 0)
        }
        amountOfPages
      }
    }
  }
}



