package ch.scotty.converter.scotty

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SongIdsToConvertFinder(implicit db: Db) {

  //TODO: Find all, because we also want to convert only lyrics. But make sure, that the Songship UI can handle this.
  def findSongIdsToConvert(): Seq[Long] = {
    val query = Tables.Filemetadata.map(_.liedId)
    val dbReadFuture = db.db.run(query.result)
    Await.result(dbReadFuture, Duration.Inf)
  }
}