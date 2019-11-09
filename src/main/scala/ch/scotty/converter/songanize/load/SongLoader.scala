package ch.scotty.converter.songanize.load

import java.sql.Timestamp

import ch.scotty.Db
import ch.scotty.converter.songanize.SonganizeSong
import slick.jdbc.GetResult
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

class SongLoader(implicit db: Db) {

  def load(songIdFilter: Option[Set[Long]]): Future[Seq[SonganizeSong]] = {

    val idFilterString = songIdFilter.map(_.mkString("where f.id IN (", ",", ")")).getOrElse("")
    val query = sql"""
      select
        f.id,
        (case when (f.timestamp = '0000-00-00 00:00:00') THEN '1970-01-01 00:00:00' ELSE f.timestamp END) as timestamp,
        f.lastchange,
        f.user_id,
        f.filename,
        f.title,
        f.song_key,
        CONCAT(uf.year, "/", uf.week, "/", uf.user_id) as path,
        f.code,
        f.type
        FROM user_files f
        join user_folder uf on (f.folder_id = uf.id)
        #$idFilterString""".as[SonganizeSong](GetResult(r =>
      SonganizeSong(r.<<, r.<<?[Timestamp].getOrElse(new Timestamp(Long.MinValue)), r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<)))

    db.db.run(query)
  }
}