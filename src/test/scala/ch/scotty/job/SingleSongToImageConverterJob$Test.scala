package ch.scotty.job

import java.util.UUID

import ch.scotty.{Db, UnitSpec}
import ch.scotty.converter._
import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration

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
    //act
    val testee = createTestee()
    testee.run(fooJobConfig)
    //assert
    (converterBySongIdStub.convert _).verify(songId)
  }

}
