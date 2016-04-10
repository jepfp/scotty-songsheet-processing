package ch.scotty

import java.io.FileInputStream
import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.tools.imageio.ImageIOUtil
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.awt.image.BufferedImage
import org.apache.pdfbox.io.IOUtils
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDDocument
import java.nio.file.Path
import java.nio.file.Paths
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.rendering.ImageType
import slick.lifted.TableQuery
import slick.driver.MySQLDriver
import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._
import slick.backend.DatabasePublisher
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {
//  def main(args: Array[String]) = {
//    println("Converting pdf...")
//    //    convertPdfToImages("./working/", "test2")
//    println("done.")
//    readLieder()
//  }
//
//  private def convertPdfToImages(inputDir: String, filename: String) = {
//    val file = new File(inputDir, filename + ".pdf")
//    val doc = PDDocument.load(new FileInputStream(file));
//    val renderer = new PDFRenderer(doc)
//    val listOfImage = for (i <- 0 until doc.getNumberOfPages()) yield {
//      println("Exporting page " + i)
//      val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
//      val path = Paths.get(inputDir, filename + "_" + i + ".gif").toString()
//      ImageIOUtil.writeImage(bim, path, 0);
//      bim
//    }
//  }
//
//  private def readLieder() = {
//    val db = Database.forConfig("scottyIntTest")
//
//    val lieds: TableQuery[Tables.Lied] = TableQuery[Tables.Lied]
//
//  }
}