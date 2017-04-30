package ch.scotty.scenario

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.UUID

import ch.scotty._
import ch.scotty.fixture.SongFixture
import ch.scotty.job.determinesongstoconvert.WriterForSingleSongToImageConverterJob
import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration
import ch.scotty.job.json.result.TestFolder

class SingleSongToImageConverterJobScenarioTest extends ScenarioSpec with TestFolder with DatabaseConnection {
  info("As a user I want to create a list of jobs with pdfs which are converted into images")
  info("so that I can use them directly inside a html page")

  private val jobsFileName = "singleSongToImageConverterJob.json"

  def assertContentEquals = ResourceFileContentWithFileContentComparator.assertContentEquals(getClass) _

  def createJobConfig(id: Long, jobId: UUID, jobsFilePathInTempFolder: Path) = {
    val jobConfig = new SingleSongToImageConverterJobConfiguration(jobId, id)
    val writerForSingleSongToImageConverterJob = new WriterForSingleSongToImageConverterJob()
    writerForSingleSongToImageConverterJob.generateAndWriteJobFile(jobsFilePathInTempFolder.toFile, Seq(jobConfig))
  }

  feature("SingleSongToImageConverterJob") {
    scenario("Happy case: Job with one song in the database that contains a convertible pdf file.") {
      Given("A song in the database that contains a pdf")
      val createdLiedRow = SongFixture.DefaultSongFixture.generateRevelationSong
      Given(s"A json input ${jobsFileName} file with a SingleSongToImageConverterJob entry for that song")
      val jobId = UUID.fromString("cc07c7e8-f160-11e6-bc64-92361f002671");
      val jobsFilePathInTempFolder = Paths.get(testFolder.getPath, jobsFileName)
      createJobConfig(createdLiedRow.id, jobId, jobsFilePathInTempFolder)

      When("scotty-songsheet-processing is started with that json input file")
      Main.main(Array[String](jobsFilePathInTempFolder.toString))


      Then(s"One result json file is being created saying that the job ${jobId} was executed successfully.")
      assertContentEquals("result_" + jobsFileName, new File(testFolder.getPath, "result_" + jobsFileName))
    }
  }

}
