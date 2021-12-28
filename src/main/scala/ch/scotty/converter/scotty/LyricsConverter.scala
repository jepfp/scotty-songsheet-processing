package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import com.typesafe.scalalogging.Logger
import slick.jdbc.MySQLProfile.api._

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

private object LyricsConverter {
  val newLine = "\r\n"
  val brTag = "<br>"
  val brWithSlashTag = "<br/>"
}

private class LyricsConverter(implicit db: Db) {

  import LyricsConverter._

  private val logger = Logger(classOf[LyricsConverter])

  case class Verse(content: Option[String], refrainId: Option[Long])

  case class Refrain(id: Long, content: String)

  def loadAndConvertLyrics(liedId: Long)(implicit ex: ExecutionContext): Future[Option[String]] =
    performQuery(liedId).map(r => {
      val formattedContent = buildContentFromVersesWithRefrain(r._1, r._2)
      logger.debug(s"formatted liedId=$liedId queryResult=$r to formatedResult=$formattedContent")
      formattedContent
    })

  @tailrec
  private def buildContentFromVersesWithRefrain(verses: Seq[Verse], refrains: Seq[Refrain], acc: Seq[String] = Seq.empty): Option[String] = {

    verses match {
      case Seq(head, tail@_*) =>
        val (r, otherRefrains) = refrains.partition(r => head.refrainId.contains(r.id))

        val refrainAndVerse = (head.content, r) match {
          case (Some(verse), r) if r.nonEmpty =>
            r.head.content + newLine + newLine + verse
          case (Some(verse), _) =>
            verse
          case (None, r) if r.nonEmpty =>
            r.head.content
          case _ =>
            ""
        }

        buildContentFromVersesWithRefrain(tail, otherRefrains, acc :+ refrainAndVerse)
      case _ =>
        val accWithNotAssignedRefrains = (acc.collect { case x if x.trim.nonEmpty => x } ++ refrains.map(_.content)).mkString(newLine + newLine)
        Option(accWithNotAssignedRefrains).collect { case x if x.trim.nonEmpty => x }
    }
  }

  private def performQuery(liedId: Long)(implicit ex: ExecutionContext): Future[(Seq[Verse], Seq[Refrain])] = {

    val versesQuery = Tables.Liedtext.filter(_.liedId === liedId).sortBy(s => (s.reihenfolge, s.id))
    val refrainsQuery = Tables.Refrain.filter(_.liedId === liedId).sortBy(s => (s.reihenfolge, s.id))

    for (
      verses <- db.db.run(versesQuery.result)
        .map(all => all
          .map(v => Verse(formatVerseOrRefrain(v.strophe), v.refrainId))
          .filter(v => v.content.nonEmpty || v.refrainId.nonEmpty)
        );
      refrains <- db.db.run(refrainsQuery.result)
        .map(all => all
          .map(e => Refrain(e.id, formatVerseOrRefrain(e.refrain).getOrElse("")))
          .filter(_.content.nonEmpty)
        )
    ) yield (verses, refrains)
  }

  private def formatVerseOrRefrain(optContent: Option[String]): Option[String] = {
    optContent
      .map(
        _.replaceAll(brTag, newLine)
          .replaceAll(brWithSlashTag, newLine)
          .trim
          .split(newLine)
          .map(_.trim)
          .mkString(newLine)
      )
      .collect { case x if x.trim.nonEmpty => x }
  }
}