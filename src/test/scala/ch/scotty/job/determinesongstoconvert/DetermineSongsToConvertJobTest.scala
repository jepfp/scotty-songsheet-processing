package ch.scotty.job.determinesongstoconvert

import java.io.File
import java.util.UUID

import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration
import ch.scotty.{Db, UnitSpec}

class DetermineSongsToConvertJobTest extends UnitSpec {

  implicit val dbStub = stub[Db]
  protected lazy val writerForSingleSongToImageConverterJobStub = stub[WriterForSingleSongToImageConverterJob]
  private val songIdsToConvertFinderStub = stub[SongIdsToConvertFinder]

  private def createTestee(): DetermineSongsToConvertJob = {
    return new DetermineSongsToConvertJob {
      protected override lazy val songIdsToConvertFinder = songIdsToConvertFinderStub
      protected override lazy val writerForSingleSongToImageConverterJob = writerForSingleSongToImageConverterJobStub
    }
  }

  "run" should "when songIdsToConvertFinder returns 5 call the writer with a singleJob for songId = 5" in {
    //arrange
    val uuid = UUID.randomUUID()
    val fooJobConfig = DetermineSongsToConvertJobConfiguration(uuid)
    (songIdsToConvertFinderStub.findSongIdsToConvert _).when().returns(Seq(5))
    //act
    val testee = createTestee()
    testee.run(fooJobConfig)
    //assert
    val matcher = (_: File, s: Seq[SingleSongToImageConverterJobConfiguration]) => {
      s(0).songId == 5
    }
    (writerForSingleSongToImageConverterJobStub.generateAndWriteJobFile _).verify(where(matcher))
  }
}
