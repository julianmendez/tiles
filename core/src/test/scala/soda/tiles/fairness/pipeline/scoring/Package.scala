package soda.tiles.fairness.pipeline.scoring

/*
 * This package contains tests for the classes to model a resource allocation scenario.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Random
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder





/**
 * This is a scoring scenario. Twenty-six people have a protected attribute, and are considered
 * for 'yes' or 'no' with respect to some unspecified query.
 */

trait ScoringScenarioExample
{



  lazy val agents : Seq [Agent] =
    Seq (
      "Alice", "Benjamin", "Charlotte", "Daniel", "Emily", "Fiona", "George", "Hannah", "Isaac",
      "James", "Kevin", "Lily", "Matthew", "Natalie", "Olivia", "Quinn", "Peter", "Rachel",
      "Sarah", "Timothy", "Ursula", "Victoria", "William", "Xavier", "Yasmine", "Zachary"
    )

  private lazy val _resource_zero : Resource = "0"

  private lazy val _resource_one : Resource = "1"

  private lazy val _measure_zero : Measure = Some (0)

  private lazy val _measure_one : Measure = Some (1)

  lazy val seed_protected_attribute : Long = 127

  lazy val protected_attribute_modulus : Int = 2

  def mod (x : Int) (modulus : Int) : Int =
    ( (x % modulus) + modulus) % modulus

  def as_protected_attribute (x : Int) : Int =
    mod (x) (protected_attribute_modulus)

  lazy val protected_attribute : Seq [Boolean] =
    Random .mk .get_next_seq (seed_protected_attribute) (agents .length)
      .map ( x => as_protected_attribute (x .intValue) == 1)

  lazy val protected_attribute_map : Map [Agent, Boolean] =
    agents
      .indices
      .map ( index =>
        Tuple2 [Agent, Boolean] (agents .apply (index) , protected_attribute .apply (index) ) )
      .toMap

  def protected_attribute_function (a : Agent) : Boolean =
    protected_attribute_map .getOrElse (a , false)

  lazy val seed_result : Long = 65535

  lazy val prediction_modulus : Int = 100

  lazy val prediction_limit : Int = prediction_modulus / 2

  def min (a : Int) (b : Int) : Int =
    if ( a < b
    ) a
    else b

  def as_prediction (x : Int) : Int =
    mod (min (x) (prediction_modulus - 1) ) (prediction_modulus)

  def make_binary_resource (x : Int) : Resource =
    if ( x > prediction_limit
    ) _resource_one
    else _resource_zero

  lazy val result_values : Seq [Int] =
    Random .mk
      .get_next_seq (seed_result) (agents .length)
      .map ( x => as_prediction ( (x .intValue) % prediction_modulus) )

  lazy val prediction_error : Int = 1

  lazy val prediction_bias_on_attribute : Int = 40

  lazy val maximum_acceptable_bias_percentage : Measure = Some (30)

  lazy val result : Seq [Resource] =
    result_values .map ( x => make_binary_resource (x) )

  lazy val result_map : Map [Agent, Resource] =
    agents
      .indices
      .map ( index =>
        Tuple2 [Agent, Resource] (agents .apply (index) , result .apply (index) ) )
      .toMap

  def result_function (a : Agent) : Resource =
    result_map .getOrElse (a , _resource_zero)

  def add_attribute_bias (index : Int) (original : Int) : Int =
    if ( (protected_attribute .apply (index) )
    ) original
    else as_prediction (original + prediction_bias_on_attribute)

  def add_little_attribute_bias (index : Int) (original : Int) : Int =
    if ( (protected_attribute .apply (index) && index < 2)
    ) original
    else as_prediction (original + prediction_bias_on_attribute)

  def add_prediction_error (value : Int) : Int =
    as_prediction (value + prediction_error)

  lazy val unbiased_prediction : Seq [Resource] =
    result_values
      .map ( x => add_prediction_error (x) )
      .map ( x => make_binary_resource (x) )

  lazy val mostly_unbiased_prediction : Seq [Resource] =
    result_values
      .indices
      .map ( index => add_little_attribute_bias (index) (result_values .apply (index) ) )
      .map ( x => add_prediction_error (x) )
      .map ( x => make_binary_resource (x) )

  lazy val biased_prediction : Seq [Resource] =
    result_values
      .indices
      .map ( index => add_attribute_bias (index) (result_values .apply (index) ) )
      .map ( x => add_prediction_error (x) )
      .map ( x => make_binary_resource (x) )

  lazy val unbiased_outcome : Outcome =
    Outcome .mk (
      agents
        .indices
        .map ( index =>
          Assignment .mk (agents .apply (index) ) (unbiased_prediction .apply (index) )
        )
    )

  lazy val mostly_unbiased_outcome : Outcome =
    Outcome .mk (
      agents
        .indices
        .map ( index =>
          Assignment .mk (agents .apply (index) ) (mostly_unbiased_prediction .apply (index) )
        )
    )

  lazy val biased_outcome : Outcome =
    Outcome .mk (
      agents
        .indices
        .map ( index =>
          Assignment .mk (agents .apply (index) ) (biased_prediction .apply (index) )
        )
    )

  lazy val positive_value : Resource = _resource_one

  lazy val context = "context"

  lazy val initial_unbiased : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (unbiased_outcome) (true)

  lazy val initial_mostly_unbiased : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (mostly_unbiased_outcome) (true)

  lazy val initial_biased : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (biased_outcome) (true)

}

case class ScoringScenarioExample_ () extends ScoringScenarioExample

object ScoringScenarioExample {
  def mk : ScoringScenarioExample =
    ScoringScenarioExample_ ()
}


case class UnbiasednessPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val ex = ScoringScenarioExample .mk

  lazy val unbiasedness_pipeline =
    UnbiasednessPipeline .mk (
      ex .positive_value) (
      ex .result_function) (
      ex .protected_attribute_function
    )

  test ("coefficient of a completely unbiased sample") (
    check (
      obtained = unbiasedness_pipeline .apply (ex .initial_unbiased) .contents
    ) (
      expected = None
    )
  )

  test ("coefficient of mostly unbiased sample") (
    check (
      obtained = unbiasedness_pipeline .apply (ex .initial_mostly_unbiased) .contents
    ) (
      expected = Some (0.08)
    )
  )

  test ("coefficient of biased sample") (
    check (
      obtained = unbiasedness_pipeline .apply (ex .initial_biased) .contents
    ) (
      expected = Some (0.42)
    )
  )

}

