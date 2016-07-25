package ch.scotty

import org.scalamock.scalatest.MockFactory
import org.scalatest._

// According to http://www.scalatest.org/user_guide/defining_base_classes
abstract class UnitSpec extends FlatSpec with Matchers with
  OptionValues with Inside with Inspectors with MockFactory