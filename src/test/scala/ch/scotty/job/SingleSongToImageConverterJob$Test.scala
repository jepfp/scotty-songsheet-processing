package ch.scotty.job

import java.util.UUID

import ch.scotty.converter._
import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration
import ch.scotty.job.json.result.{Failure, Success}
import ch.scotty.{Db, UnitSpec}

class SingleSongToImageConverterJob$Test extends UnitSpec {

  implicit val dbStub = stub[Db]

  private val converterBySongIdStub = stub[ConverterBySongId]

  private def createTestee(): SingleSongToImageConverterJob = {
    return new SingleSongToImageConverterJob {
      protected override lazy val converterBySongId = converterBySongIdStub
    }
  }

  "run" should "execute conversion with found songnumers" in {
    //arrange
    val songId: Long = 4
    val fooJobConfig = SingleSongToImageConverterJobConfiguration(UUID.randomUUID(), songId)
    (converterBySongIdStub.convert _).when(songId).returns(ConversionResults.Success(None))
    //act
    val testee = createTestee()
    testee.run(fooJobConfig)
    //assert
    (converterBySongIdStub.convert _).verify(songId)
  }

  it should "return Success in case of a successful conversion" in {
    //arrange
    val songId: Long = 4
    val jobUuid = UUID.randomUUID
    val fooJobConfig = SingleSongToImageConverterJobConfiguration(jobUuid, songId)
    (converterBySongIdStub.convert _).when(songId).returns(ConversionResults.Success(None))
    //act
    val testee = createTestee()
    val result = testee.run(fooJobConfig)
    //assert
    assertResult(Right(Success(jobUuid)))(result)
  }

  it should "return Failure in case of a failed conversion" in {
    //arrange
    val songId: Long = 4
    val jobUuid = UUID.randomUUID
    val fooJobConfig = SingleSongToImageConverterJobConfiguration(jobUuid, songId)
    val message = "foo"
    val exception = new Exception()
    (converterBySongIdStub.convert _).when(songId).returns(ConversionResults.FailedConversionWithException(message, exception))
    //act
    val testee = createTestee()
    val result = testee.run(fooJobConfig)
    //assert
    assertResult(Left(Failure(jobUuid, message, Seq(exception))))(result)
  }
}
