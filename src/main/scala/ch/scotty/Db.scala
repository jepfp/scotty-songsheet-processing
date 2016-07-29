package ch.scotty

import slick.driver.MySQLDriver

trait Db {
    val db : MySQLDriver.backend.Database
}
