package ch.scotty.converter.blob

import java.awt.image.BufferedImage

import ch.scotty.converter.effect._
import ch.scotty.converter.{FileType, SourceSystem}
import com.typesafe.scalalogging.Logger
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

import scala.util.Try

private[converter] class PdfBlobConverter(override val exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) extends BlobConverter {

  private final val outputFileType = "png"

  val logger = Logger(classOf[PdfBlobConverter])

  override def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String, inputFileType : FileType): Try[BlobConverterResult] = {
    Try {
      val doc: PDDocument = PDDocument.load(data)
      using(doc) { doc =>
        val renderer = new PDFRenderer(doc)
        val amountOfPages = doc.getNumberOfPages
        for (i <- 0 until amountOfPages) yield {
          logger.debug("Exporting songId={}, pageIndex={} of {}", songId, i, amountOfPages)
          val bim: BufferedImage = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
          ImageIOUtil.writeImage(bim, generatePathString(sourceSystem, songId, i, dataChecksum, outputFileType), 0)
        }
        BlobConverterResult(amountOfPages, outputFileType);
      }
    }
  }
}



