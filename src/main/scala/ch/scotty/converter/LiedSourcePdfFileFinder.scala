package ch.scotty.converter

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object LiedSourcePdfFileFinder {
  val filetype = "sourcepdf"

  def findFile(liedId: Long): LiedWithData = {
    require(liedId > 0, "liedId must be set")

    val joinQuery = for {
      f <- Tables.File
      fm <- f.filemetadataFk if fm.filetype === filetype
      l <- fm.liedFk if l.id === liedId
    } yield (l.id, l.titel, f.data)
    
//    println("Going to execute query: " + joinQuery.result.statements)

    val dbReadFuture = Db.db.run(joinQuery.result)
    val result: Seq[LiedWithData] = Await.result(dbReadFuture, Duration.Inf).map(x => LiedWithData.tupled(x))
    throwExceptionIfMoreThanOneResult(result, liedId)
    result.head
  }

  private def throwExceptionIfMoreThanOneResult(result: Seq[LiedWithData], liedId: Long) = {
    if (result.length > 1) {
      throw new ConverterException(s"More than one PDF source file found for liedId = ${liedId}")
    }
  }

}