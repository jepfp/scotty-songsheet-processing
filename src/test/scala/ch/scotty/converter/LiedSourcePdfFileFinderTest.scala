package ch.scotty.converter

import ch.scotty.{Db, UnitSpec}
import org.scalatest.EitherValues

class LiedSourcePdfFileFinderTest extends UnitSpec with EitherValues{
  implicit val db = stub[Db]

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
    val testee = createTesteeAndOverridePerformQuery(Seq(LiedWithData(50, "foo", null), LiedWithData(50, "blub", null)))
    //act & assert
    val result = testee.findFile(50).left.value
    assertResult("More than one PDF source file found for liedId = 50")(result)
  }
}
