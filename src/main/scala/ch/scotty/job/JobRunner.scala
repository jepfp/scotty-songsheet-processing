package ch.scotty.job

import ch.scotty.{Db, Stopwatch}
import ch.scotty.job.json.{JobConfiguration, JobDefinitions}
import ch.scotty.job.json.result.PerJobDefinitionResultHolder

import scala.collection.mutable

class JobRunner (private val jobDefinitions: JobDefinitions, private val singleSongToImageConverterJob : SingleSongToImageConverterJob, private val allSongToImageConverterJob : AllSongToImageConverterJob)(implicit val db: Db) {
  private val jobResults = new mutable.HashMap[String, PerJobDefinitionResultHolder]()

  def this(jobDefinitions: JobDefinitions)(implicit db: Db){
    this(jobDefinitions, new SingleSongToImageConverterJob(), new AllSongToImageConverterJob())
  }

  def runAllJobs(): Map[String, PerJobDefinitionResultHolder] = {
    val sw = Stopwatch.time("All Jobs") {
      runJobAndStoreResult(allSongToImageConverterJob)
      runJobAndStoreResult(singleSongToImageConverterJob)
    }
    println(s"\nAll jobs have been executed. Duration: $sw")
    jobResults.toMap
  }

  private def runJobAndStoreResult(jobToRun: Job[_]) = {
    val jobResult = jobToRun.runIfJobsDefined(jobDefinitions)
    val className  = jobToRun.getClass.getSimpleName
    jobResults +=(className -> jobResult)
  }

}
