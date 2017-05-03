package ch.scotty.converter

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

private class LiedSourcePdfFileFinder(implicit db: Db) {
  private val filetype = "sourcepdf"

  def findFile(liedId: Long): Either[String, LiedWithData] = {
    require(liedId > 0, "liedId must be set")
    val result: Seq[LiedWithData] = performQuery(liedId)
    result.length match {
      case x if x < 1 => Left(s"Could not find a PDF source file for liedId = $liedId")
      case 1 => Right(result.head)
      case x if x > 1 => Left(s"More than one PDF source file found for liedId = $liedId")
    }
  }

  //VisibleForTesting
  protected def performQuery(liedId: Long): Seq[LiedWithData] = {
    val joinQuery = for {
      f <- Tables.File
      fm <- f.filemetadataFk if fm.filetype === filetype
      l <- fm.liedFk if l.id === liedId
    } yield (l.id, l.titel, f.data)
    val dbReadFuture = db.db.run(joinQuery.result)
    val result: Seq[LiedWithData] = Await.result(dbReadFuture, Duration.Inf).map(x => LiedWithData.tupled(x))
    result
  }
}