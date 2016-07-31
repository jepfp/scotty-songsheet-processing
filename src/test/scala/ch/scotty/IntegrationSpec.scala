package ch.scotty

import ch.scotty.fixture.RefDataFixture
import ch.scotty.generatedschema.Tables
import slick.driver.MySQLDriver.api._
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// Done according to http://www.superloopy.io/articles/2013/scala-slick-postgresql-unit-tests.html
abstract class IntegrationSpec extends FlatSpec{
  private val schemaName = "scottymole_inttest"
  implicit val db = new Db {
    override val db = Database.forConfig("scotty-mole-integration-testing")
  }

  private val dbWithoutConfig = Database.forConfig("scotty-mole-integration-testing-dbsetup")
  createTestDatabaseWithSchema()
  val refDataFixture = new RefDataFixture(db)
  refDataFixture.insertRefData()



  private def createTestDatabaseWithSchema() = {
    val setupAction: DBIO[Unit] = DBIO.seq(
      sqlu"""drop database if exists #$schemaName""",
      sqlu"""create database #$schemaName""",
      sqlu"""use #$schemaName""",
      Tables.schema.create
    )
    val future = dbWithoutConfig.run(setupAction)
    Await.result(future, Duration.Inf)
  }
}