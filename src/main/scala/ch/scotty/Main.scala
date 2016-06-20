package ch.scotty

import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.tools.imageio.ImageIOUtil

import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._
import slick.lifted.Query
import slick.lifted.Rep
import slick.lifted.TableQuery
import ch.scotty.job.Job
import ch.scotty.job.JobParser
import ch.scotty.converter.LiedSourcePdfFileFinder
import ch.scotty.converter.LiedWithData

import net.java.truecommons.io.Loan._

object Main {
  def main(args: Array[String]) = {
    val jobs = readJobs()
    println(s"Read ${jobs.length} jobs.")
    println("Converting PDFs...")
    val sw = Stopwatch.time("Converting all PDFs") {
      //    convertPdfToImages("./working/", "test2")

      jobs.par.foreach { aJob =>
        val liedIdToFetch = aJob.liedId
        println(s"Going to read ${liedIdToFetch}...")
        val liedData = LiedSourcePdfFileFinder.findFile(liedIdToFetch)
        convertPdfBlobToImage(liedData)
      }
    }
    println(s"Finished: ${sw}")
    Db.db.close()

    //readLieder()
  }

  private def convertPdfToImagesFromInputDir(inputDir: String, filename: String) = {
    val file = new File(inputDir, filename + ".pdf")
    val doc = PDDocument.load(new FileInputStream(file));
    val renderer = new PDFRenderer(doc)
    val listOfImage = for (i <- 0 until doc.getNumberOfPages()) yield {
      println("Exporting page " + i)
      val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
      val path = Paths.get(inputDir, filename + "_" + i + ".gif").toString()
      ImageIOUtil.writeImage(bim, path, 0);
    }
    println("Finished")
  }

  private def convertPdfBlobToImage(liedWithData: LiedWithData) = {
    try {
      val binaryStream = liedWithData.data.getBinaryStream
      loan(PDDocument.load(binaryStream)).to { doc =>
        //val doc = PDDocument.load(binaryStream);
        val renderer = new PDFRenderer(doc)
        val listOfImage = for (i <- 0 until doc.getNumberOfPages()) yield {
          println(s"Exporting '${liedWithData.titel}' page " + (i + 1))
          val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
          val titelWithOnlyAllowedCharacters = liedWithData.titel.replaceAll("[^a-zA-Z0-9äöüÄÖÜ .]", "_")
          val filename = liedWithData.liedId + "-" + titelWithOnlyAllowedCharacters + "-" + i + ".png"
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

  private def readLieder() = {
    val db = Db.db

    //val lieds: TableQuery[Tables.Lied] = TableQuery[Tables.Lied]
    val users: TableQuery[Tables.User] = TableQuery[Tables.User]
    val lieds = Tables.Lied

    val joinQuery2 = for {
      l <- lieds
      s <- users if l.lastedituserId === s.id
    } yield (l.titel, s.firstname)

    try {
      val joinQuery = lieds.join(users).on(_.lastedituserId === _.id)

      Await.result(db.run(DBIO.seq(
        lieds.result.map(r => {
          for (aRecord <- r) {
            println
            println("Titel: " + aRecord.titel)
            println("Tonart: " + aRecord.tonality)
          }
        }),
        lieds.filter(_.id === 1L).result.map(result => result.foreach(aRow => println("Lied mit ID 1: " + aRow.titel))),
        lieds.filter(_.id === 1L).map { case (titel) => titel }.result.map { println },
        lieds.map(_.titel).result.map(println),
        joinQuery2.result.map(println),
        joinQuery.result.map(println) //
        )), Duration.Inf)
    } finally db.close
  }

  def readJobs(): Seq[Job] = {
    val source = scala.io.Source.fromFile("jobs.json")
    val content = try source.mkString finally source.close()
    val parser = new JobParser()
    parser.parseJobJson(content)
  }
}