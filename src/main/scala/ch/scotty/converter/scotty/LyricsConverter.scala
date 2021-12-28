package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import com.typesafe.scalalogging.Logger
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

private object LyricsConverter {
  val newLine = "\r\n"
  val brTag = "<br>"
  val brWithSlashTag = "<br/>"
}

private class LyricsConverter(implicit db: Db) {

  import LyricsConverter._

  val logger = Logger(classOf[LyricsConverter])

  case class VerseWithRefrain(verse: Option[String], refrain: Option[String])

  def loadAndConvertLyrics(liedId: Long)(implicit ex: ExecutionContext): Future[Option[String]] =
    performQuery(liedId).map(buildContentFromVersesWithRefrain)

  private def buildContentFromVersesWithRefrain(versesWithRefrain: Seq[VerseWithRefrain]): Option[String] = {
    val formattedContent: String = versesWithRefrain.map(formatSingleVerseWithRefrain)
      .collect { case Some(value) => value }
      .mkString(newLine + newLine)

    Option(formattedContent).collect { case x if x.trim.nonEmpty => x }
  }

  private def formatSingleVerseWithRefrain(vwr: VerseWithRefrain): Option[String] = {
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

  protected def performQuery(liedId: Long)(implicit ex: ExecutionContext): Future[Seq[VerseWithRefrain]] = {
    val joinQuery = for {
      (l, r) <- Tables.Liedtext joinLeft Tables.Refrain on (_.refrainId === _.id) if l.liedId === liedId
    } yield (l.strophe, r.flatMap(_.refrain))
    db.db.run(joinQuery.result)
      .map(result => result.map(entry => VerseWithRefrain(entry._1, entry._2)))
  }
}