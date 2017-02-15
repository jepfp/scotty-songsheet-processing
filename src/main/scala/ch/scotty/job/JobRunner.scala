package ch.scotty.job

import ch.scotty.job.determinesongstoconvert.DetermineSongsToConvertJob
import ch.scotty.job.json.JobDefinitions
import ch.scotty.job.json.result.PerJobDefinitionResultHolder
import ch.scotty.{Db, Stopwatch}

import scala.collection.mutable

class JobRunner (private val jobDefinitions: JobDefinitions, private val singleSongToImageConverterJob : SingleSongToImageConverterJob, private val allSongToImageConverterJob : AllSongToImageConverterJob, private val determineSongsToConvertJob : DetermineSongsToConvertJob)(implicit val db: Db) {
  private val jobResults = new mutable.HashMap[String, PerJobDefinitionResultHolder]()

  def this(jobDefinitions: JobDefinitions)(implicit db: Db){
    this(jobDefinitions, new SingleSongToImageConverterJob(), new AllSongToImageConverterJob(), new DetermineSongsToConvertJob())
  }

  def runAllJobs(): Map[String, PerJobDefinitionResultHolder] = {
    val sw = Stopwatch.time("All Jobs") {
      runJobAndStoreResult(allSongToImageConverterJob)
      runJobAndStoreResult(singleSongToImageConverterJob)
      runJobAndStoreResult(determineSongsToConvertJob)
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
