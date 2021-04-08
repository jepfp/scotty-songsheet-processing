package ch.scotty

import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// According to http://www.scalatest.org/user_guide/defining_base_classes
abstract class UnitSpec extends AnyFlatSpec with Matchers with
  OptionValues with Inside with Inspectors with MockFactory