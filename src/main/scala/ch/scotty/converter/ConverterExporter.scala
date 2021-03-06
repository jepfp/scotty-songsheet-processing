package ch.scotty.converter

import java.util.zip.{CRC32, CheckedInputStream, Checksum}

import better.files.DisposeableExtensions
import ch.scotty.converter.blob.{BlobConverter, NoOpBlobConverter, PdfBlobConverter}
import ch.scotty.converter.effect._
import com.typesafe.scalalogging.Logger
import org.apache.commons.io.IOUtils

import scala.util.{Failure, Success, Try}

/**
 * - Converter for both songship and songanize songs.
 * - Conditional: Only exports files (overwrite), if the checksums of the binary data streams don't match
 */
class ConverterExporter(exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) {

  val tableOfContentsFileReader: TableOfContentsFileReader = new TableOfContentsFileReader(exportPathResolverAndCreator)
  val tableOfContentsFileCreator: TableOfContentsFileCreator = new TableOfContentsFileCreator(exportPathResolverAndCreator)

  val logger = Logger(classOf[ConverterExporter])

  def convertAndExport(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]): Try[TableOfContentsDTOs.Song] = {
    Try {
      val conversionResult: Try[TableOfContentsDTOs.Song] = for (
        c <- createConverter(liedWithData);
        s <- convertAndPerformExportIfNecessary(liedWithData, songnumbers, c)
      ) yield s

      conversionResult recoverWith {
        case t: Throwable =>
          val m = s"Failure while exporting $liedWithData."
          Failure(new Exception(m, t))
      }
    }.flatten
  }

  private def createConverter(liedWithData: LiedWithData) = {
    liedWithData.fileType match {
      case FileType.Pdf(_) =>
        Success(new PdfBlobConverter(exportPathResolverAndCreator))
      case FileType.Image(_) =>
        Success(new NoOpBlobConverter(exportPathResolverAndCreator))
      case FileType.Unknown(unknownType) =>
        Failure(new Exception(s"Detected unknown file type which cannot be converted: fileType=${
          unknownType
        } songId=${
          liedWithData.songId
        } title=${
          liedWithData.title
        }"))
    }
  }

  private def convertAndPerformExportIfNecessary(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], converter: BlobConverter): Try[TableOfContentsDTOs.Song] = {

    val (content: Array[Byte], checksum: Checksum) = loadContentWithChecksum(liedWithData)
    val tocReadResult = tableOfContentsFileReader.readTableOfContentsFile(liedWithData.sourceSystem, liedWithData.songId)
    val checksumString = checksum.getValue.toString
    (tocReadResult match {
      case Right(t) if t.pdfSourceChecksum == checksumString =>
        logger.debug(s"No conversion for id=${
          liedWithData.songId
        } title='${
          liedWithData.title
        }' necessary. Checksums were equal.")
        tableOfContentsFileCreator.createFile(liedWithData, songnumbers, t.amountOfPages, t.pdfSourceChecksum, t.versionTimestamp, t.fileType)
      case _ =>
        logger.debug(s"Exporting id=${liedWithData.songId} title='${liedWithData.title}'. Checksums did not match. Checksum database: ${checksum.getValue} Current toc read result: ${tocReadResult}")
        converter.convertToImages(liedWithData.sourceSystem, liedWithData.songId, content, checksumString, liedWithData.fileType)
          .flatMap(r => tableOfContentsFileCreator.createFile(liedWithData, songnumbers, r.amountOfPages, checksumString, checksumString, r.fileTypeAfterExport))

    })
  }

  private def loadContentWithChecksum(liedWithData: LiedWithData) = {
    val (content, checksum) = liedWithData.data.getBinaryStream.autoClosed.apply(stream => {
      val checkedInputStream = new CheckedInputStream(stream, new CRC32())
      val byteArray = IOUtils.toByteArray(checkedInputStream)
      (byteArray, checkedInputStream.getChecksum)
    })
    (content, checksum)
  }
}



