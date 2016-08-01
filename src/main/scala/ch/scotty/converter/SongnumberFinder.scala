package ch.scotty.converter

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

class SongnumberFinder(implicit db : Db) {

  def findSongnumbers(liedId: Long): Seq[Songnumber] = {
    require(liedId > 0, "liedId must be set")
    val joinQuery = for {
      ll <- Tables.Fkliederbuchlied if ll.liedId === liedId
      liederbuch <- ll.liederbuchFk
    } yield (ll.liedId, liederbuch.id, liederbuch.mnemonic, liederbuch.buchname, ll.liednr)
    val dbReadFuture = db.db.run(joinQuery.result)
    val result: Seq[Songnumber] = Await.result(dbReadFuture, Duration.Inf).map(x => Songnumber.tupled(x))
    result
  }

}