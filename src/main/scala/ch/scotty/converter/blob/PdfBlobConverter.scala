package ch.scotty.converter.blob

import ch.scotty.converter.effect._
import ch.scotty.converter.{FileType, SongsheetConfig, SourceSystem}
import com.typesafe.scalalogging.Logger
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

import java.awt.image.BufferedImage
import scala.util.Try

private[converter] class PdfBlobConverter(override val exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) extends BlobConverter {

  private final val outputFileType = "png"

  val logger = Logger(classOf[PdfBlobConverter])

  override def convertToImages(sourceSystem: SourceSystem, songId: Long, data: Array[Byte], dataChecksum: String, inputFileType: FileType): Try[BlobConverterResult] = {

    val pdfConfig = SongsheetConfig.get().pdf
    Try {
      val doc: PDDocument = PDDocument.load(data)
      using(doc) { doc =>
        val renderer = new PDFRenderer(doc)
        val amountOfPages = doc.getNumberOfPages
        for (i <- 0 until amountOfPages) yield {
          logger.debug("Exporting songId={}, pageIndex={} of {}", songId, i, amountOfPages)
          val bim: BufferedImage = renderer.renderImageWithDPI(i, pdfConfig.dpi, ImageType.RGB)
          writeUncompressedFileIfConfigured(sourceSystem, songId, dataChecksum, i, bim)
          compressAndPersist(sourceSystem, songId, dataChecksum, i, bim)
        }
        BlobConverterResult(amountOfPages, outputFileType);
      }
    }
  }

  private def writeUncompressedFileIfConfigured(sourceSystem: SourceSystem, songId: Long, dataChecksum: String, i: Int, bim: BufferedImage) = {
    if (SongsheetConfig.get().persistUncompressedImage) {
      val originalFileOutputPath = generatePathStringWithSuffix(sourceSystem, songId, i, dataChecksum, "uncompressed", outputFileType)
      ImageIOUtil.writeImage(bim, originalFileOutputPath, 0)
    } else {
      logger.debug("configuration for persisting uncompressed image is set to false --> skipping")
    }
  }

  private def compressAndPersist(sourceSystem: SourceSystem, songId: Long, dataChecksum: String, i: Int, bim: BufferedImage) = {
    val fileOutputPath = generatePathString(sourceSystem, songId, i, dataChecksum, outputFileType)
    new PngCompressorAndPersister().compressAndPersist(bim, fileOutputPath)
  }
}



