package ch.scotty

import ch.scotty.generatedschema.Tables
import ch.scotty.job.JobRunner
import ch.scotty.job.json.{JobDefinitions, JobParser}
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main {
  val jobsPath : String = "jobs.json"

  implicit val db = new DefaultDb()

  def main(args: Array[String]) = {
    println(s"Reading job definitions...")
    val jobDefinitions: JobDefinitions = JobParser.parseJobJson(readJsonFile())
    println("Running jobs...")
    val jobRunner = new JobRunner(jobDefinitions)
    jobRunner.runAllJobs()
    db.db.close()
  }

  def readJsonFile(): String = {
    val source = scala.io.Source.fromFile(jobsPath)
    try source.mkString finally source.close()
  }

  private def readLieder() = {

    //val lieds: TableQuery[Tables.Lied] = TableQuery[Tables.Lied]
    val users: TableQuery[Tables.User] = TableQuery[Tables.User]
    val lieds = Tables.Lied

    val joinQuery2 = for {
      l <- lieds
      s <- users if l.lastedituserId === s.id
    } yield (l.titel, s.firstname)

    try {
      val joinQuery = lieds.join(users).on(_.lastedituserId === _.id)

      Await.result(db.db.run(DBIO.seq(
        lieds.result.map(r => {
          for (aRecord <- r) {
            println
            println("Titel: " + aRecord.titel)
            println("Tonart: " + aRecord.tonality)
          }
        }),
        lieds.filter(_.id === 1L).result.map(result => result.foreach(aRow => println("Lied mit ID 1: " + aRow.titel))),
        lieds.filter(_.id === 1L).map { case (titel) => titel }.result.map {
          println
        },
        lieds.map(_.titel).result.map(println),
        joinQuery2.result.map(println),
        joinQuery.result.map(println) //
      )), Duration.Inf)
    } finally db.db.close
  }
}