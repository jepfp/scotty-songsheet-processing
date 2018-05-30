package ch.scotty.converter

import java.time.LocalDateTime

import ch.scotty.UnitSpec

class FilenameBuilder$Test extends UnitSpec {

  private val aDateTime: LocalDateTime = LocalDateTime.of(2014, 5, 29, 7, 22)
  val songnumberAl1000: Songnumber = Songnumber(4, 2, "AL", "Adoray Liederordner", "1000")
  val liedAbc = LiedWithData(4, "Abc", Some("C"), aDateTime, aDateTime.plusYears(4), null)

  "build" should s"return '4-0-AL1000-Abc-.png' for $liedAbc and $songnumberAl1000" in {
    // arrange
    val expectedString = "4-0-AL1000-Abc-.png"
    // act
    val actualString = FilenameBuilder.build(liedAbc, Seq(songnumberAl1000), 0)
    // assert
    assertResult(expectedString)(actualString)
  }

  it should s"return '4-1-AL1000-Abc-.png' for sheetnumber 1" in {
    // arrange
    val expectedString = "4-1-AL1000-Abc-.png"
    // act
    val actualString = FilenameBuilder.build(liedAbc, Seq(songnumberAl1000), 1)
    // assert
    assertResult(expectedString)(actualString)
  }

  it should s"return '4-1-AL1000_ZG1-Abc-.png' for two songnumbers" in {
    // arrange
    val expectedString = "4-1-AL1000_ZG1-Abc-.png"
    // act
    val actualString = FilenameBuilder.build(liedAbc, Seq(songnumberAl1000, Songnumber(4, 3, "ZG", "Adoray Zug", "1")), 1)
    // assert
    assertResult(expectedString)(actualString)
  }

  it should "not remove the characters .azAZ09äöüÄÖÜ in a title" in {
    // arrange
    val expectedString = "4-1-AL1000-.azAZ09äöüÄÖÜ-.png"
    // act
    val actualString = FilenameBuilder.build(LiedWithData(4, ".azAZ09äöüÄÖÜ", Some("C"), aDateTime, aDateTime.plusYears(4), null), Seq(songnumberAl1000), 1)
    // assert
    assertResult(expectedString)(actualString)
  }

  it should "remove e. g. the characters _-$/%& in a title" in {
    // arrange
    val expectedString = "4-1-AL1000--.png"
    // act
    val actualString = FilenameBuilder.build(LiedWithData(4, "_-$/%&", Some("C"), aDateTime, aDateTime.plusYears(4), null), Seq(songnumberAl1000), 1)
    // assert
    assertResult(expectedString)(actualString)
  }

}
