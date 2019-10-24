package ch.scotty.converter


import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.{CRC32, CheckedInputStream, Checksum}

import better.files.DisposeableExtensions
import ch.scotty.converter.ConversionResults.{ConversionResult, FailedConversionWithException, Success}
import com.typesafe.scalalogging.Logger
import org.apache.commons.io.IOUtils
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.apache.pdfbox.tools.imageio.ImageIOUtil

private class ConditionalLiedPdfToImageConverter(exportPathResolverAndCreator: ExportPathResolverAndCreator = new ExportPathResolverAndCreator()) {

  val tableOfContentsFileReader: TableOfContentsFileReader = new TableOfContentsFileReader(exportPathResolverAndCreator)
  val tableOfContentsFileCreator: TableOfContentsFileCreator = new TableOfContentsFileCreator(exportPathResolverAndCreator)

  val logger = Logger(classOf[ConditionalLiedPdfToImageConverter])

  def convertPdfBlobToImage(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]): ConversionResult = {
    try {
      val stream: InputStream = liedWithData.data.getBinaryStream
      loadPdfConvertAndSaveAllPages(liedWithData, songnumbers)
    } catch {
      case e: Exception =>
        val m = s"Error while exporting $liedWithData. Song is skipped."
        logger.warn(m, e)
        FailedConversionWithException(m + "Error: " + e, e)
    }
  }

  private def loadPdfConvertAndSaveAllPages(liedWithData: LiedWithData, songnumbers: Seq[Songnumber]) = {

    val (content: Array[Byte], checksum: Checksum) = loadContentWithChecksum(liedWithData)
    val tocReadResult = tableOfContentsFileReader.readTableOfContentsFile(liedWithData.songId)
    tocReadResult match {
      case Right(t) if t.pdfSourceChecksum == checksum.getValue.toString =>
        Success(Some(s"No conversion for id=${liedWithData.songId} title='${liedWithData.title}' necessary. Checksums were equal."))
      case _ =>
        println(s"Exporting id=${liedWithData.songId} title='${liedWithData.title}'. Checksums did not match. Checksum database: ${checksum.getValue} Current toc read result: ${tocReadResult}")
        val amountOfPages = exportPdf(liedWithData, songnumbers, content, checksum)
        tableOfContentsFileCreator.createFile(liedWithData, songnumbers, amountOfPages, checksum.getValue.toString, formatUpdatedAtDate(LocalDateTime.now()))
    }
  }

  private def exportPdf(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], content: Array[Byte], checksum: Checksum) = {
    val doc = PDDocument.load(content)
    val renderer = new PDFRenderer(doc)
    val amountOfPages = doc.getNumberOfPages
    for (i <- 0 until amountOfPages) yield {
      println(s"Exporting '${liedWithData.title}' page " + (i + 1))
      val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
      ImageIOUtil.writeImage(bim, generatePathString(liedWithData, songnumbers, i), 0)
    }
    doc.close()
    amountOfPages
  }

  private def loadContentWithChecksum(liedWithData: LiedWithData) = {
    val (content, checksum) = liedWithData.data.getBinaryStream.autoClosed.apply(stream => {
      val checkedInputStream = new CheckedInputStream(stream, new CRC32())
      val byteArray = IOUtils.toByteArray(checkedInputStream)
      (byteArray, checkedInputStream.getChecksum)
    })
    (content, checksum)
  }

  private def formatUpdatedAtDate(updatedAt: LocalDateTime) = {
    updatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
  }

  private def generatePathString(liedWithData: LiedWithData, songnumbers: Seq[Songnumber], i: Int) = {
    val filename = IdTimestampFilenameBuilder.build(liedWithData, songnumbers, i)
    val path = exportPathResolverAndCreator.resolve(filename)
    path
  }
}



