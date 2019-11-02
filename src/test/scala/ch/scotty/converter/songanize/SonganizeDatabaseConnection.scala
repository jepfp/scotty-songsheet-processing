package ch.scotty.converter.songanize

import better.files.File
import ch.scotty.Db
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, TestSuite}
import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// Done according to http://www.superloopy.io/articles/2013/scala-slick-postgresql-unit-tests.html
trait SonganizeDatabaseConnection extends TestSuite with BeforeAndAfterEach with BeforeAndAfterAll {
  private val schemaName = "songanize_inttest"
  private final val referenceDataFilename = "referenceData.sql"

  //This is done once per test class as scalatest does not create a new instance for every test.
  //Note that db.db can stay open even though before each test a new db is created...
  implicit val db = new Db {
    override val db = Database.forConfig("songanize-integration-testing")
  }

  override def afterAll(): Unit = {
    db.db.close()
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    createTestDatabaseWithSchemaAndData()
  }

  private def loadTableDefinitionsAndContent() = {
    val expectedFile: File = File(getClass.getResource(referenceDataFilename).toURI)
    expectedFile.contentAsString.split(";")
  }


  private def createTestDatabaseWithSchemaAndData() = {
    val dbWithoutConfig = Database.forConfig("songanize-integration-testing-dbsetup")
    println(s"Starting setup of int test db (db: $schemaName)...")

    val statements = Seq(
      sqlu"""drop database if exists #$schemaName""",
      sqlu"""create database #$schemaName""",
      sqlu"""use #$schemaName"""
    ) ++ loadTableDefinitionsAndContent().map(query => sqlu"""#$query""")

    val future = dbWithoutConfig.run(DBIO.seq(statements: _*))
    Await.result(future, Duration.Inf)
    dbWithoutConfig.close()
  }
}
