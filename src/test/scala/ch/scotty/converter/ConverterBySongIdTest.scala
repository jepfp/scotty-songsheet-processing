package ch.scotty.converter

import ch.scotty.{Db, UnitSpec}

class ConverterBySongIdTest extends UnitSpec {
  implicit val dbStub = stub[Db]

  private val liedSourcePdfFileFinderStub = stub[LiedSourcePdfFileFinder]
  private val songnumberFinderStub = stub[SongnumberFinder]
  private val liedPdfToImageConverterStub = stub[LiedPdfToImageConverter]

  private def createTestee(): ConverterBySongId = {
    return new ConverterBySongId {
      protected override lazy val liedSourcePdfFileFinder = liedSourcePdfFileFinderStub
      protected override lazy val songnumberFinder = songnumberFinderStub
      protected override lazy val liedPdfToImageConverter = liedPdfToImageConverterStub
    }
  }

  "run" should "execute conversion with found songnumers" in {
    //arrange
    val songId : Long = 4
    val songnumbers: Seq[Songnumber] = Seq(Songnumber(songId, 5, "LU", "Adoray Luzern", "299"))
    val liedWithData: LiedWithData = LiedWithData(songId, "foo", null)

    (liedSourcePdfFileFinderStub.findFile _).when(songId).returns(liedWithData)
    (songnumberFinderStub.findSongnumbers(_)).when(songId).returns(songnumbers)
    //act
    val testee = createTestee()
    testee.convert(songId)
    //assert
    (liedPdfToImageConverterStub.convertPdfBlobToImage _).verify(LiedWithData(songId, "foo", null), songnumbers)
  }
}
