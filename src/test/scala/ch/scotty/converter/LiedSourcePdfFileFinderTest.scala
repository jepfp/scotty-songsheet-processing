package ch.scotty.converter

import ch.scotty.{Db, UnitSpec}
import slick.driver.MySQLDriver

class LiedSourcePdfFileFinderTest extends UnitSpec {
  implicit val db = stub[Db]

  private def createTesteeAndOverridePerformQuery(performQueryImplementation: Seq[LiedWithData]) : LiedSourcePdfFileFinder = {
    new LiedSourcePdfFileFinder(){
      override def performQuery(liedId: Long): Seq[LiedWithData] = performQueryImplementation
    }
  }

  "findFile" should "throw an exception if no PDF source file for a given liedId can be found" in {
    //arrange
    val testee = createTesteeAndOverridePerformQuery(Seq.empty)
    //act & assert
    val message = intercept[ConverterException](testee.findFile(50)).getMessage
    assertResult("Could not find a PDF source file for liedId = 50")(message)
  }

  "findFile" should "throw an exception if more than one PDF source file for a given liedId can be found" in {
    //arrange
    val testee = createTesteeAndOverridePerformQuery(Seq(LiedWithData(50, "foo", null), LiedWithData(50, "blub", null)))
    //act & assert
    val message = intercept[ConverterException](testee.findFile(50)).getMessage
    assertResult("More than one PDF source file found for liedId = 50")(message)
  }
}
