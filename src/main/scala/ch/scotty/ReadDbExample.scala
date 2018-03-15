package ch.scotty

import ch.scotty.generatedschema.Tables
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object ReadDbExample extends App {

  // the base query for the Users table
  val lieds = TableQuery[Tables.Lied]

  val db = Database.forConfig("scottyIntTest")
  try {
    Await.result(db.run(DBIO.seq(

      // print the users (select * from USERS)
      lieds.result.map(println)
    )), Duration.Inf)
  } finally db.close
}
