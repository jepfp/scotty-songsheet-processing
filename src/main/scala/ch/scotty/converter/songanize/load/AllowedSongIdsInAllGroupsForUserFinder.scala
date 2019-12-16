package ch.scotty.converter.songanize.load

import ch.scotty.Db
import ch.scotty.converter.songanize.effect.SongForUser
import slick.jdbc.GetResult
import slick.jdbc.MySQLProfile.api._
import slick.sql.SqlStreamingAction

import scala.concurrent.Future

class AllowedSongIdsInAllGroupsForUserFinder(implicit db: Db) {

  def find(userId: Long): Future[Seq[SongForUser]] = {

    val query: SqlStreamingAction[Vector[SongForUser], SongForUser, Effect] = sql"""
      select groups.group_name, song_groups.song_id from wp_users u
        join groups_users ON (u.id = groups_users.user_id)
          join groups ON (groups_users.group_id = groups.id)
          join song_groups ON (groups.id = song_groups.group_id)
          where u.id = $userId
      union select "Private", id
          from user_files
          where user_id = $userId""".as[SongForUser](GetResult(r => SongForUser(r.<<, r.<<)))

    db.db.run(query)
  }
}