package ch.scotty.converter

import ch.scotty.converter.ConversionResults.Success
import ch.scotty.{Db, UnitSpec}

class ConverterBySongIdTest extends UnitSpec {
  implicit val dbStub = stub[Db]

  val songId: Long = 4
  val songnumbers: Seq[Songnumber] = Seq(Songnumber(songId, 5, "LU", "Adoray Luzern", "299"))
  val liedWithData: LiedWithData = LiedWithData(songId, "foo", null)

  private val liedSourcePdfFileFinderStub = stub[LiedSourcePdfFileFinder]
  private val songnumberFinderStub = stub[SongnumberFinder]
  private val liedPdfToImageConverterStub = stub[LiedPdfToImageConverter]

  private def createTestee(): ConverterBySongId = {
    return new ConverterBySongId {
      protected[converter] override lazy val liedSourcePdfFileFinder = liedSourcePdfFileFinderStub
      protected[converter] override lazy val songnumberFinder = songnumberFinderStub
      protected[converter] override lazy val liedPdfToImageConverter = liedPdfToImageConverterStub
    }
  }

  "run" should "execute conversion with found songnumers" in {
    //arrange
    trainStubs
    //act
    val testee = createTestee()
    testee.convert(songId)
    //assert
    (liedPdfToImageConverterStub.convertPdfBlobToImage _).verify(LiedWithData(songId, "foo", null), songnumbers)
  }

  private def trainStubs = {
    (liedSourcePdfFileFinderStub.findFile _).when(songId).returns(Right(liedWithData))
    (songnumberFinderStub.findSongnumbers(_)).when(songId).returns(songnumbers)
  }

  it should "forward the return value from liedPdfToImageConverter" in {
    //arrange
    trainStubs
    (liedPdfToImageConverterStub.convertPdfBlobToImage _).when(*, *).returns(Success())
    //act
    val testee = createTestee()
    val result = testee.convert(songId)
    //assert
    assertResult(Success())(result)
  }
}
