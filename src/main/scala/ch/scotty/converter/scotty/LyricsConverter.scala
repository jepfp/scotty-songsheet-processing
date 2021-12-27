package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import com.typesafe.scalalogging.Logger
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

private object LyricsConverter{
  val newLine = "\r\n"
  val brTag = "<br>"
  val brWithSlashTag = "<br/>"
}

private class LyricsConverter(implicit db: Db) {
  import LyricsConverter._

  val logger = Logger(classOf[LyricsConverter])

  case class VerseWithRefrain(verse: Option[String], refrain: Option[String])

  def loadAndConvertLyrics(liedId: Long): Option[String] = {
    val formattedContent: String = performQuery(liedId)
      .map(formatContent)
      .collect { case Some(value) => value }
      .mkString(newLine + newLine)

    Option(formattedContent).collect { case x if x.trim.nonEmpty => x }
  }

  private def formatContent(vwr: VerseWithRefrain): Option[String] = {
    val verseOpt = vwr.verse.map(_
      .replaceAll(brTag, newLine)
      .replaceAll(brWithSlashTag, newLine)
      .trim
    )
      .collect { case x if x.trim.nonEmpty => x }
      .map(_.split(newLine).map(_.trim).mkString(newLine))

    val refrainOpt = vwr.refrain.map(_
      .replaceAll(brTag, newLine)
      .replaceAll(brWithSlashTag, newLine)
      .trim
    )
      .collect { case x if x.trim.nonEmpty => x }
      .map(_.split(newLine).map(_.trim).mkString(newLine))

    val content = (verseOpt, refrainOpt) match {
      case (Some(verse), Some(refrain)) =>
        Some(refrain + newLine + newLine + verse)
      case (Some(verse), None) =>
        Some(verse)
      case (None, Some(refrain)) =>
        Some(refrain)
      case _ =>
        None
    }

    logger.debug(s"formatted $vwr to $content")
    content
  }

  protected def performQuery(liedId: Long): Seq[VerseWithRefrain] = {
    val joinQuery = for {
      (l, r) <- Tables.Liedtext joinLeft Tables.Refrain on (_.refrainId === _.id)
    } yield (l.strophe, r.flatMap(_.refrain))
    val dbReadFuture = db.db.run(joinQuery.result)
    val result: Seq[VerseWithRefrain] = Await.result(dbReadFuture, Duration.Inf).map(x => VerseWithRefrain(x._1, x._2))
    result
  }
}