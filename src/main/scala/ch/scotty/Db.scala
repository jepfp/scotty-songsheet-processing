package ch.scotty

import slick.jdbc.MySQLProfile

trait Db {
    val db : MySQLProfile.api.Database
}
