package ch.scotty.converter.scotty

import ch.scotty.converter.{FileType, LiedWithData, SourceSystem}
import ch.scotty.{Db, UnitSpec}
import org.scalatest.TryValues

import java.time.LocalDateTime

class LiedSourcePdfFileFinderTest extends UnitSpec with TryValues {
  implicit val db = stub[Db]
  private val aDateTime: LocalDateTime = LocalDateTime.of(2014, 5, 29, 7, 22)

  private def createTesteeAndOverridePerformQuery(performQueryImplementation: Seq[LiedWithData]): LiedSourcePdfFileFinder = {
    new LiedSourcePdfFileFinder() {
      override def performQuery(liedId: Long): Seq[LiedWithData] = performQueryImplementation
    }
  }

  "findFile" should "return a failure if no PDF source file for a given liedId can be found" in {
    //arrange
    val testee = createTesteeAndOverridePerformQuery(Seq.empty)
    //act & assert
    val result = testee.findFile(50).failure.exception
    result.getMessage shouldBe "Could not find a PDF source file for liedId = 50"
  }

  "findFile" should "return an either left if more than one PDF source file for a given liedId can be found" in {
    //arrange
    val testee = createTesteeAndOverridePerformQuery(Seq(LiedWithData(SourceSystem.Scotty, 50, "foo", Some("C"), List.empty, aDateTime, aDateTime.plusYears(4), null, FileType.Pdf(), None), LiedWithData(SourceSystem.Scotty, 50, "blub", Some("C"), List.empty, aDateTime, aDateTime.plusYears(4), null, FileType.Pdf(), None)))
    //act & assert
    val result = testee.findFile(50).failure.exception
    result.getMessage shouldBe "More than one PDF source file found for liedId = 50"
  }
}
