package ch.scotty

import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.tools.imageio.ImageIOUtil
import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._
import slick.lifted.Query
import slick.lifted.Rep
import slick.lifted.TableQuery
import ch.scotty.generatedschema.ManLied
import scala.concurrent.duration.Duration

object Main {
  def main(args: Array[String]) = {
    println("Converting pdf...")
    //    convertPdfToImages("./working/", "test2")
    println("done.")
    readLieder()
  }

  private def convertPdfToImages(inputDir: String, filename: String) = {
    val file = new File(inputDir, filename + ".pdf")
    val doc = PDDocument.load(new FileInputStream(file));
    val renderer = new PDFRenderer(doc)
    val listOfImage = for (i <- 0 until doc.getNumberOfPages()) yield {
      println("Exporting page " + i)
      val bim = renderer.renderImageWithDPI(i, 90, ImageType.RGB)
      val path = Paths.get(inputDir, filename + "_" + i + ".gif").toString()
      ImageIOUtil.writeImage(bim, path, 0);
      bim
    }
  }

  private def readLieder() = {
    val db = Database.forConfig("scottyIntTest")

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
}