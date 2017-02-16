package ch.scotty

class Main$Test extends UnitSpec {

  "main" should "when the program is called with a not existing file path throw an exception" in {
    //arrange
    //act & assert
    intercept[IllegalArgumentException](Main.main(Array [String]("notExistingFile.json")))
  }

}
