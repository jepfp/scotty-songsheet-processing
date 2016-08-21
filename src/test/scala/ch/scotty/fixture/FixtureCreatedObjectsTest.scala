package ch.scotty.fixture

import ch.scotty.UnitSpec

class FixtureCreatedObjectsTest extends UnitSpec {

  private def testAddRowAndFindRow: Unit = {
    //arrange
    val testee = new FixtureCreatedObjects()
    val aCaseClass: FooClass = FooClass(1, "abc")
    //act
    testee.addRow(aCaseClass)
    //assert
    assertResult(aCaseClass)(testee.findRow(classOf[FooClass]))
  }

  "addRow" should "add a row to the map" in {
    testAddRowAndFindRow
  }

  case class FooClass(val id: Long, val name: String);
  case class Foo2Class(val id: Long, val name: String);

  "findRow" should "find one row if added" in {
    testAddRowAndFindRow
  }

  "findRow" should "throw an exception if there is no case class for a type" in {
    //arrange
    val testee = new FixtureCreatedObjects()
    val aCaseClass: FooClass = FooClass(1, "abc")
    //act
    testee.addRow(aCaseClass)
    //assert
    intercept[NoSuchElementException](testee.findRow(classOf[Foo2Class]))
  }

  "addRow" should "throw an exception if there is already a case class of that type" in {
    //arrange
    val testee = new FixtureCreatedObjects()
    val aCaseClass: FooClass = FooClass(1, "abc")
    testee.addRow(aCaseClass)
    //act & assert
    val message = intercept[IllegalArgumentException](testee.addRow(aCaseClass)).getMessage
    assertResult("You must not add an element of the same class twice to the list!")(message)
  }

}
