package ch.scotty.job

import java.util.UUID

import ch.scotty.UnitSpec
import ch.scotty.converter._
import ch.scotty.job.json.SingleSongToImageConverterJobConfiguration

class SingleSongToImageConverterJob$Test extends UnitSpec {

  val liedSourcePdfFileFinderStub = stub[LiedSourcePdfFileFinder]
  val songnumberFinderStub = stub[SongnumberFinder]
  val liedPdfToImageConverterStub = stub[LiedPdfToImageConverter]

  private def createTestee(): SingleSongToImageConverterJob = {
    return new SingleSongToImageConverterJob {
      protected override lazy val liedSourcePdfFileFinder = liedSourcePdfFileFinderStub
      protected override lazy val songnumberFinder = songnumberFinderStub
      protected override lazy val liedPdfToImageConverter = liedPdfToImageConverterStub

    }
  }

  "run" should "execute conversion with found songnumers" in {
    //arrange
    val songId : Long = 4
    val fooJobConfig = SingleSongToImageConverterJobConfiguration(UUID.randomUUID(), songId)
    val songnumbers: Seq[Songnumber] = Seq(Songnumber(songId, 5, "LU", "Adoray Luzern", "299"))
    val liedWithData: LiedWithData = LiedWithData(songId, "foo", null)

    (liedSourcePdfFileFinderStub.findFile _).when(songId).returns(liedWithData)
    (songnumberFinderStub.findSongnumbers(_)).when(songId).returns(songnumbers)
    //act
    val testee = createTestee()
    testee.run(fooJobConfig)
    //assert
    (liedPdfToImageConverterStub.convertPdfBlobToImage _).verify(LiedWithData(songId, "foo", null), songnumbers)
  }

}
