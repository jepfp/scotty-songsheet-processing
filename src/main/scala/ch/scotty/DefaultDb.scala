package ch.scotty


import slick.driver.MySQLDriver.api._


class DefaultDb extends Db{
//  val db = Database.forConfig("exampleScotty")
  val db = Database.forConfig("dockerdefault")
}