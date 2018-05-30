package ch.scotty.converter

import java.time.LocalDateTime

import ch.scotty.{Db, UnitSpec}
import org.scalatest.EitherValues

class LiedSourcePdfFileFinderTest extends UnitSpec with EitherValues{
  implicit val db = stub[Db]
  private val aDateTime: LocalDateTime = LocalDateTime.of(2014, 5, 29, 7, 22)

  private def createTesteeAndOverridePerformQuery(performQueryImplementation: Seq[LiedWithData]) : LiedSourcePdfFileFinder = {
    new LiedSourcePdfFileFinder(){
      override def performQuery(liedId: Long): Seq[LiedWithData] = performQueryImplementation
    }
  }

  "findFile" should "return an either left if no PDF source file for a given liedId can be found" in {
    //arrange
    val testee = createTesteeAndOverridePerformQuery(Seq.empty)
    //act & assert
    val result = testee.findFile(50).left.value
    assertResult("Could not find a PDF source file for liedId = 50")(result)
  }

  "findFile" should "return an either left if more than one PDF source file for a given liedId can be found" in {
    //arrange
    val testee = createTesteeAndOverridePerformQuery(Seq(LiedWithData(50, "foo", Some("C"), aDateTime, aDateTime.plusYears(4), null), LiedWithData(50, "blub", Some("C"), aDateTime, aDateTime.plusYears(4), null)))
    //act & assert
    val result = testee.findFile(50).left.value
    assertResult("More than one PDF source file found for liedId = 50")(result)
  }
}
