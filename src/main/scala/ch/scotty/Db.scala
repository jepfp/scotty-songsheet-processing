package ch.scotty


import slick.driver.MySQLDriver.api._


object Db {
  val db = Database.forConfig("exampleScotty")
}