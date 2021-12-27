package ch.scotty.converter.scotty

import ch.scotty.converter._
import ch.scotty.converter.effect.TableOfContentsDTOs
import ch.scotty.{Db, UnitSpec}

import java.time.LocalDateTime
import scala.util.Success

class ConverterBySongIdTest extends UnitSpec {
  implicit val dbStub = stub[Db]

  private val aDateTime: LocalDateTime = LocalDateTime.of(2014, 5, 29, 7, 22)
  val songId: Long = 4
  val songnumbers: Seq[Songnumber] = Seq(Songnumber(songId, 5, "LU", "Adoray Luzern", "299"))
  private val fileType = FileType.Pdf()
  val liedWithData: LiedWithData = LiedWithData(SourceSystem.Scotty, songId, "foo", Some("C"), List.empty, aDateTime, aDateTime.plusYears(4), null, fileType, None)
  val effectSong = TableOfContentsDTOs.Song(SourceSystem.Scotty.getIdentifier, songId, liedWithData.title, liedWithData.tonality, songnumbers.map(s => TableOfContentsDTOs.Songnumber(s.liederbuchId, s.mnemonic, s.liederbuch, s.liednr)), Seq.empty, 2, "someChecksum", "someVersionTimestamp", aDateTime, aDateTime.plusYears(4), fileType.concreteExtension)

  private val liedSourcePdfFileFinderStub = stub[LiedSourcePdfFileFinder]
  private val songnumberFinderStub = stub[SongnumberFinder]
  private val converterExporterStub = stub[ConverterExporter]

  private def createTestee(): ConverterBySongId = {
    new ConverterBySongId {
      protected[converter] override lazy val liedSourcePdfFileFinder = liedSourcePdfFileFinderStub
      protected[converter] override lazy val songnumberFinder = songnumberFinderStub
      protected[converter] override lazy val converterExporter = converterExporterStub
    }
  }

  "run" should "execute conversion with found songnumers" in {
    //arrange
    trainStubs
    //act
    val testee = createTestee()
    testee.convert(songId)
    //assert
    (converterExporterStub.convertAndExport _).verify(LiedWithData(SourceSystem.Scotty, songId, "foo", Some("C"), List.empty, aDateTime, aDateTime.plusYears(4), null, FileType.Pdf(), None), songnumbers)
  }

  private def trainStubs = {
    (liedSourcePdfFileFinderStub.findFile _).when(songId).returns(Success(liedWithData))
    (songnumberFinderStub.findSongnumbers(_)).when(songId).returns(songnumbers)
  }

  it should "forward the return value from liedPdfToImageConverter" in {
    //arrange
    trainStubs
    (converterExporterStub.convertAndExport _).when(*, *).returns(Success(effectSong))
    //act
    val testee = createTestee()
    val result = testee.convert(songId)
    //assert
    result shouldBe Success(effectSong)
  }
}
