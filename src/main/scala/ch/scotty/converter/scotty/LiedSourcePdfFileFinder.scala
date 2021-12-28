package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.converter.{FileType, LiedWithData, SourceSystem}
import ch.scotty.generatedschema.Tables
import slick.jdbc.MySQLProfile.api._

import java.sql.{Blob, Timestamp}
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

private class LiedSourcePdfFileFinder(implicit db: Db) {
  private val filetype = "sourcepdf"

  private def getSingleElement(liedId: Long, liedAndFileDbReadFuture: Future[Seq[(Long, String, Option[String], Option[Timestamp], Option[Timestamp], Blob)]]): Future[(Long, String, Option[String], Option[Timestamp], Option[Timestamp], Blob)] = {
    liedAndFileDbReadFuture.flatMap(result => result.length match {
      case x if x < 1 => Future.failed(new IllegalStateException(s"Could not find a PDF source file for liedId = $liedId"))
      case 1 => Future.successful(result.head)
      case x if x > 1 => Future.failed(new IllegalStateException(s"More than one PDF source file found for liedId = $liedId"))
    })
  }

  def performQuery(liedId: Long): Try[LiedWithData] = {
    val joinQuery = for {
      f <- Tables.File
      fm <- f.filemetadataFk if fm.filetype === filetype
      l <- fm.liedFk if l.id === liedId
    } yield (l.id, l.titel, l.tonality, l.createdAt, l.updatedAt, f.data)

    val lyricsDbReadFuture = new LyricsConverter().loadAndConvertLyrics(liedId)
    val liedAndFileDbReadFuture = db.db.run(joinQuery.result)

    val combinedFuture = for (
      lyrics <- lyricsDbReadFuture;
      liedAndFile <- getSingleElement(liedId, liedAndFileDbReadFuture)
    ) yield LiedWithData(
      SourceSystem.Scotty,
      liedAndFile._1,
      liedAndFile._2,
      liedAndFile._3,
      List.empty[String],
      liedAndFile._4.map(_.toLocalDateTime).getOrElse(LocalDateTime.MIN),
      liedAndFile._5.map(_.toLocalDateTime).getOrElse(LocalDateTime.MIN),
      liedAndFile._6,
      FileType.Pdf(),
      lyrics)

    Try(Await.result(combinedFuture, Duration.Inf))
  }
}