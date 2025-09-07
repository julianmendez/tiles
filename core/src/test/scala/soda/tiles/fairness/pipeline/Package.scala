package soda.tiles.fairness.pipeline

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





case class EqualityPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val example = ResourceAllocationScenarioExample .mk

  lazy val equality_pipeline = EqualityPipeline .mk (example .resource_height)

  test ("equality on outcome 0") (
    check (
      obtained = equality_pipeline .apply (example .initial0) .contents
    ) (
      expected = false
    )
  )

  test ("equality on outcome 1") (
    check (
      obtained = equality_pipeline .apply (example .initial1) .contents
    ) (
      expected = true
    )
  )

  test ("equality on outcome 2") (
    check (
      obtained = equality_pipeline .apply (example .initial2) .contents
    ) (
      expected = true
    )
  )

  test ("equality on outcome 3") (
    check (
      obtained = equality_pipeline .apply (example .initial3) .contents
    ) (
      expected = true
    )
  )

}


case class EquityPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val example = ResourceAllocationScenarioExample .mk

  lazy val equity_pipeline =
    EquityPipeline .mk (example .agent_need) (example .resource_height)

  test ("equity on outcome 0") (
    check (
      obtained = equity_pipeline .apply (example .initial0) .contents
    ) (
      expected = true
    )
  )

  test ("equity on outcome 1") (
    check (
      obtained = equity_pipeline .apply (example .initial1) .contents
    ) (
      expected = false
    )
  )

  test ("equity on outcome 2") (
    check (
      obtained = equity_pipeline .apply (example .initial2) .contents
    ) (
      expected = false
    )
  )

  test ("equity on outcome 3") (
    check (
      obtained = equity_pipeline .apply (example .initial3) .contents
    ) (
      expected = true
    )
  )

}


/**
 * This is a resource allocation scenario. There are three people of different heights. The may
 * need boxes to stand on them and to watch above a wall.
 */

trait ResourceAllocationScenarioExample
{



  lazy val resource0 = "small box - 0.1 m"

  lazy val resource1 = "medium box - 0.2 m"

  lazy val resource2 = "large box - 0.3 m"

  lazy val agent0 = "Anna A"

  lazy val agent1 = "Bob B"

  lazy val agent2 = "Charlie C"

  lazy val agent_need_map : Map [Agent, Measure] = Seq (
    Tuple2 [Agent, Measure] (agent0 , Some (30) ) ,
    Tuple2 [Agent, Measure] (agent1 , Some (10) ) ,
    Tuple2 [Agent, Measure] (agent2 , Some (0) )
  ) .toMap

  lazy val resource_height_map : Map [Resource, Measure] = Seq (
    Tuple2 [Resource, Measure] (resource0 , Some (10) ) ,
    Tuple2 [Resource, Measure] (resource1 , Some (20) ) ,
    Tuple2 [Resource, Measure] (resource2 , Some (30) )
  ) .toMap

  def agent_need (agent : Agent) : Measure =
    agent_need_map .getOrElse (agent , Some (-1) )

  def resource_height (resource : Resource) : Measure =
    resource_height_map .getOrElse (resource , Some (-1) )

  lazy val context = "context"

  lazy val outcome0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource0)
      )
    )

  lazy val outcome1 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource0) ,
        Assignment .mk (agent1) (resource0) ,
        Assignment .mk (agent2) (resource0)
      )
    )

  lazy val outcome2 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource1) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource1)
      )
    )

  lazy val outcome3 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource2) ,
        Assignment .mk (agent2) (resource2)
      )
    )

  lazy val initial0 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome0) (true)

  lazy val initial1 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome1) (true)

  lazy val initial2 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome2) (true)

  lazy val initial3 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome3) (true)

}

case class ResourceAllocationScenarioExample_ () extends ResourceAllocationScenarioExample

object ResourceAllocationScenarioExample {
  def mk : ResourceAllocationScenarioExample =
    ResourceAllocationScenarioExample_ ()
}


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

  lazy val protected_attribute : Seq [Measure] =
    Random .mk .get_next_seq (seed_protected_attribute) (agents .length)
      .map ( x => Some ( as_protected_attribute (x .intValue) ) )

  lazy val protected_attribute_map : Map [Agent, Measure] =
    agents
      .indices
      .map ( index =>
        Tuple2 [Agent, Measure] (agents .apply (index) , protected_attribute .apply (index) ) )
      .toMap

  def protected_attribute_function (a : Agent) : Measure =
    protected_attribute_map .getOrElse (a , _measure_zero )

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

  def make_binary_measure (x : Int) : Measure =
    if ( x > prediction_limit
    ) _measure_one
    else _measure_zero

  lazy val result_values : Seq [Int] =
    Random .mk
      .get_next_seq (seed_result) (agents .length)
      .map ( x => as_prediction ( (x .intValue) % prediction_modulus) )

  lazy val prediction_error : Int = 1

  lazy val prediction_bias_on_attribute : Int = 40

  lazy val maximum_acceptable_bias_percentage : Measure = Some (30)

  lazy val result : Seq [Measure] =
    result_values .map ( x => make_binary_measure (x) )

  lazy val result_map : Map [Agent, Measure] =
    agents
      .indices
      .map ( index =>
        Tuple2 [Agent, Measure] (agents .apply (index) , result .apply (index) ) )
      .toMap

  def result_function (a : Agent) : Measure =
    result_map .getOrElse (a , _measure_zero )

  def add_attribute_bias (index : Int) (original : Int) : Int =
    if ( (protected_attribute .apply (index) == _measure_zero)
    ) original
    else as_prediction (original + prediction_bias_on_attribute)

  def add_prediction_error (value : Int) : Int =
    as_prediction (value + prediction_error)

  lazy val unbiased_prediction : Seq [Resource] =
    result_values
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

  lazy val biased_outcome : Outcome =
    Outcome .mk (
      agents
        .indices
        .map ( index =>
          Assignment .mk (agents .apply (index) ) (biased_prediction .apply (index) )
        )
    )

  def evaluation (resource : Resource) : Measure =
    if ( (resource == _resource_zero)
    ) _measure_zero
    else _measure_one

  lazy val context = "context"

  lazy val initial_unbiased : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (unbiased_outcome) (true)

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
      ex .evaluation) (
      ex .result_function) (
      ex .protected_attribute_function) (
      ex .maximum_acceptable_bias_percentage
    )

  def get_coefficient (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    unbiasedness_pipeline .get_correlation_plumbing (
      unbiasedness_pipeline .all_agent_tile .apply (message)
    ) (
      unbiasedness_pipeline .all_agent_tile .apply (message)
    ) (
      unbiasedness_pipeline .all_agent_tile .apply (message)
    )

  test ("unbiasedness on unbiased sample") (
    check (
      obtained = unbiasedness_pipeline .apply (ex .initial_unbiased) .contents
    ) (
      expected = true
    )
  )

  test ("unbiasedness on biased sample") (
    check (
      obtained = unbiasedness_pipeline .apply (ex .initial_biased) .contents
    ) (
      expected = false
    )
  )

  test ("coefficient of unbiased sample") (
    check (
      obtained = get_coefficient (ex .initial_unbiased) .contents
    ) (
      expected = Some (0)
    )
  )

  test ("coefficient of biased sample") (
    check (
      obtained = get_coefficient (ex .initial_biased) .contents
    ) (
      expected = Some (42)
    )
  )

}

