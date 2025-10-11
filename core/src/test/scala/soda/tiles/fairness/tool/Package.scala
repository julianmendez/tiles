package soda.tiles.fairness.tool

/*
 * This package contains test for the classes to model a fairness scenario.
 */

import   org.scalatest.funsuite.AnyFunSuite





case class MeasureModSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val mod = MeasureMod .mk

  test ("zero should be Some (0)") (
    check(
      obtained = mod .zero
    ) (
      expected = Some (0)
    )
  )

  test ("one should be Some (1)") (
    check(
      obtained = mod .one
    ) (
      expected = Some (1)
    )
  )

  test ("is_equals_0 should return true for Some (0)") (
    check(
      obtained = mod .is_equals_0 (Some (0) )
    ) (
      expected = true
    )
  )

  test ("is_equals_0 should return false for Some (5)") (
    check(
      obtained = mod .is_equals_0 (Some (5) )
    ) (
      expected = false
    )
  )

  test ("add_value_to should add correctly when Measure is Some") (
    check(
      obtained = mod .add_value_to (3) (Some (7) )
    ) (
      expected = Some (10)
    )
  )

  test ("add_value_to should return None when Measure is None") (
    check(
      obtained = mod .add_value_to (3) (None)
    ) (
      expected = None
    )
  )

  test ("plus should sum two Some values") (
    check(
      obtained = mod .plus(Some (4) ) (Some (6) )
    ) (
      expected = Some (10)
    )
  )

  test ("plus should return None if first argument is None") (
    check(
      obtained = mod .plus(None) (Some (6) )
    ) (
      expected = None
    )
  )

  test ("divide_by should divide correctly") (
    check(
      obtained = mod .divide_by (Some (10) ) (2)
    ) (
      expected = Some (5)
    )
  )

  test ("divide_by should return None when dividing by zero") (
    check(
      obtained = mod .divide_by (Some (10) ) (0)
    ) (
      expected = None
    )
  )

  test ("divide should divide two Measures") (
    check(
      obtained = mod .divide (Some (10) ) (Some (2) )
    ) (
      expected = Some (5)
    )
  )

  test ("divide should return None if divisor is None") (
    check(
      obtained = mod .divide (Some (10) ) (None)
    ) (
      expected = None
    )
  )

}


case class ScoringToolSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val example_xlist_1 : Seq [Number] = Seq (1 , 3 , 5 , 8)

  lazy val example_ylist_1 : Seq [Number] = Seq (1 , 3 , 5 , 8)

  lazy val instance_1 : SeqPair = SeqPair .mk (example_xlist_1) (example_ylist_1)

  private lazy val _mt : MathTool = MathTool .mk

  private lazy val _mc : ScoringCategory = ScoringCategory .mk

  test ("sum") (
    check (
      obtained = _mt .sum (example_xlist_1)
    ) (
      expected = 17
    )
  )

  test ("average") (
    check (
      obtained = _mt .average (example_xlist_1)
    ) (
      expected = 4.25
    )
  )

  test ("categorize") (
    check (
      obtained = _mc .categorize (0.2)
    ) (
      expected = _mc .weak_positive_correlation
    )
  )

  lazy val example_xlist_2 : Seq [Number] = Seq (43 , 21 , 25 , 42 , 57 , 59)

  lazy val example_ylist_2 : Seq [Number] = Seq (99 , 65 , 79 , 75 , 87 , 81)

  lazy val instance_2 : SeqPair = SeqPair .mk (example_xlist_2) (example_ylist_2)

  test ("coefficient v1") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrDirect .mk .coefficient (instance_2) ) (0.529808901) (0.000000001)
    ) (
      expected = true
    )
  )

  test ("coefficient v2") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrCovariance .mk .coefficient (instance_2) ) (0.529808901) (0.000000001)
    ) (
      expected = true
    )
  )

  lazy val example_xlist_3 : Seq [Number] = Seq (10 , 20 , 30 , 40 , 50)

  lazy val example_ylist_3 : Seq [Number] = Seq (15 , 25 , 35 , 45 , 55)

  lazy val instance_3 : SeqPair = SeqPair .mk (example_xlist_3) (example_ylist_3)

  test ("coefficient v1 - perfect positive") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrDirect .mk .coefficient (instance_3) ) (1.0) (0.000001)
    ) (
      expected = true
    )
  )

  test ("coefficient v2 - perfect positive") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrCovariance .mk .coefficient (instance_3) ) (1.0) (0.000001)
    ) (
      expected = true
    )
  )

  lazy val example_xlist_4 : Seq [Number] = Seq (1 , 2 , 3 , 4 , 5)

  lazy val example_ylist_4 : Seq [Number] = Seq (10 , 8 , 6 , 4 , 2)

  lazy val instance_4 : SeqPair = SeqPair .mk (example_xlist_4) (example_ylist_4)

  test ("coefficient v1 - perfect negative") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrDirect .mk .coefficient (instance_4) ) (-1.0) (0.000001)
    ) (
      expected = true
    )
  )

  test ("coefficient v2 - perfect negative") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrCovariance .mk .coefficient (instance_4) ) (-1.0) (0.000001)
    ) (
      expected = true
    )
  )

  lazy val example_xlist_5 : Seq [Number] = Seq (1 , 2 , 3 , 4 , 5)

  lazy val example_ylist_5 : Seq [Number] = Seq (7 , 3 , 6 , 2 , 8)

  lazy val instance_5 : SeqPair = SeqPair .mk (example_xlist_5) (example_ylist_5)

  test ("coefficient v1 - no correlation") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrDirect .mk .coefficient (instance_5) ) (0.061084722) (0.000000001)
    ) (
      expected = true
    )
  )

  test ("coefficient v2 - no correlation") (
    check (
      obtained = MathTool .mk .is_within (PearsonCorrCovariance .mk .coefficient (instance_5) ) (0.061084722) (0.000000001)
    ) (
      expected = true
    )
  )

}


case class StringComparatorSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val empty_string : String = ""

  lazy val string0 : String = "an"

  lazy val string1 : String = "example"

  lazy val string2 : String = "z"

  lazy val instance : StringComparator = StringComparator .mk

  def sign (a : Int) : Int =
    if ( a > 0
    ) 1
    else
      if ( a < 0
      ) -1
      else 0

  test ("compare equal strings 0") (
    check (
      obtained = instance .compare (empty_string) (empty_string)
    ) (
      expected = 0
    )
  )

  test ("compare equal strings 1") (
    check (
      obtained = instance .compare (string0) (string0)
    ) (
      expected = 0
    )
  )

  test ("compare equal strings 2") (
    check (
      obtained = instance .compare (string1) (string1)
    ) (
      expected = 0
    )
  )

  test ("compare equal strings 3") (
    check (
      obtained = instance .compare (string2) (string2)
    ) (
      expected = 0
    )
  )

  test ("compare smaller strings 0") (
    check (
      obtained = instance .compare (empty_string) (string0)
    ) (
      expected = -1
    )
  )

  test ("compare smaller strings 0 compareTo") (
    check (
      obtained = instance .compare (empty_string) (string0)
    ) (
      expected = sign (empty_string .compareTo (string0) )
    )
  )

  test ("compare smaller strings 1") (
    check (
      obtained = instance .compare (string0) (empty_string)
    ) (
      expected = 1
    )
  )

  test ("compare smaller strings 1 compareTo") (
    check (
      obtained = instance .compare (string0) (empty_string)
    ) (
      expected = sign (string0 .compareTo (empty_string) )
    )
  )

  test ("compare smaller strings 2") (
    check (
      obtained = instance .compare (string0) (string2)
    ) (
      expected = -1
    )
  )

  test ("compare smaller strings 2 compareTo") (
    check (
      obtained = instance .compare (string0) (string2)
    ) (
      expected = sign (string0 .compareTo (string2) )
    )
  )

  test ("compare smaller strings 3") (
    check (
      obtained = instance .compare (string2) (string0)
    ) (
      expected = 1
    )
  )

  test ("compare smaller strings 3 compareTo") (
    check (
      obtained = instance .compare (string2) (string0)
    ) (
      expected = sign (string2 .compareTo (string0) )
    )
  )

  test ("compare smaller strings 4") (
    check (
      obtained = instance .compare (string0) (string1)
    ) (
      expected = -1
    )
  )

  test ("compare smaller strings 4 compareTo") (
    check (
      obtained = instance .compare (string0) (string1)
    ) (
      expected = sign (string0 .compareTo (string1) )
    )
  )

  test ("compare smaller strings 5") (
    check (
      obtained = instance .compare (string1) (string0)
    ) (
      expected = 1
    )
  )

  test ("compare smaller strings 5 compareTo") (
    check (
      obtained = instance .compare (string1) (string0)
    ) (
      expected = sign (string1 .compareTo (string0) )
    )
  )

}

