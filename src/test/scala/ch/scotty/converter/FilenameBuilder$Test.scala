package ch.scotty.converter

import java.sql.Blob

import ch.scotty.UnitSpec

class FilenameBuilder$Test extends UnitSpec {

  val songnumberAl1000: Songnumber = Songnumber(4, 2, "AL", "Adoray Liederordner", "1000")
  val liedAbc = LiedWithData(4, "Abc", null)

  "build" should s"return '4-0-AL1000-Abc-.png' for ${liedAbc} and ${songnumberAl1000}" in {
    // arrange
    val expectedString = "4-0-AL1000-Abc-.png"
    // act
    val actualString = FilenameBuilder.build(LiedWithData(4, "Abc", null), Seq(songnumberAl1000), 0)
    // assert
    assertResult(expectedString)(actualString)
  }

  it should s"return '4-1-AL1000-Abc-.png' for sheetnumber 1" in {
    // arrange
    val expectedString = "4-1-AL1000-Abc-.png"
    // act
    val actualString = FilenameBuilder.build(LiedWithData(4, "Abc", null), Seq(songnumberAl1000), 1)
    // assert
    assertResult(expectedString)(actualString)
  }

  it should s"return '4-1-AL1000_ZG1-Abc-.png' for two songnumbers" in {
    // arrange
    val expectedString = "4-1-AL1000_ZG1-Abc-.png"
    // act
    val actualString = FilenameBuilder.build(LiedWithData(4, "Abc", null), Seq(songnumberAl1000, Songnumber(4, 3, "ZG", "Adoray Zug", "1")), 1)
    // assert
    assertResult(expectedString)(actualString)
  }

}
