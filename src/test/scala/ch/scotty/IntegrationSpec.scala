package ch.scotty

import ch.scotty.fixture.RefDataFixture
import ch.scotty.generatedschema.Tables
import org.scalatest._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// Done according to http://www.superloopy.io/articles/2013/scala-slick-postgresql-unit-tests.html
abstract class IntegrationSpec extends FlatSpec with BeforeAndAfterEach with BeforeAndAfterAll {
  private val schemaName = "scottymole_inttest"

  //This is done once per test class as scalatest does not create a new instance for every test.
  //Note that db.db can stay open even though before each test a new db is created...
  implicit val db = new Db {
    override val db = Database.forConfig("scotty-mole-integration-testing")
  }

  override def afterAll(): Unit = {
    db.db.close()
  }

  override def beforeEach() {
    createTestDatabaseWithSchema()
    val refDataFixture = new RefDataFixture()
    refDataFixture.insertRefData()
  }

  private def createTestDatabaseWithSchema() = {
    val dbWithoutConfig: _root_.slick.driver.MySQLDriver.backend.DatabaseDef = Database.forConfig("scotty-mole-integration-testing-dbsetup")
    println(s"Starting setup of int test db (db: $schemaName)...")
    val setupAction: DBIO[Unit] = DBIO.seq(
      sqlu"""drop database if exists #$schemaName""",
      sqlu"""create database #$schemaName""",
      sqlu"""use #$schemaName""",
      Tables.schema.create
    )
    val future = dbWithoutConfig.run(setupAction)
    Await.result(future, Duration.Inf)
    dbWithoutConfig.close()
  }
}