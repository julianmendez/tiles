package soda.tiles.fairness.example.childcaresubsidy

/*
 * This package contains tests for the classes to model a resource allocation scenario.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder





case class CcsNoSubsidyPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  private lazy val _mm = ChildCareSubsidyScenarioExample .mk

  lazy val all_cases = _mm .all_cases

  lazy val no_subsidy_pipeline =
    CcsNoSubsidyPipeline .mk (_mm .measure_sum) (_mm .resource_value)

  test ("no subsidy on all outcomes") (
    check (
      obtained = all_cases
        .map ( scenario =>
          no_subsidy_pipeline .apply (scenario) .contents
        )
    ) (
      expected = Seq (true , false , false , false , false , false , false , false)
    )
  )

}


case class CcsPerChildPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  private lazy val _mm = ChildCareSubsidyScenarioExample .mk

  lazy val all_cases = _mm .all_cases

  lazy val per_child_pipeline =
    CcsPerChildPipeline .mk (_mm .measure_sum) (_mm .agent_children) (_mm .resource_value)

  test ("per child on all outcomes") (
    check (
      obtained = all_cases
        .map ( scenario =>
          per_child_pipeline .apply (scenario) .contents
        )
    ) (
      expected = Seq (true , false , false , false , true , false , false , true)
    )
  )

}


case class CcsPerFamilyPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  private lazy val _mm = ChildCareSubsidyScenarioExample .mk

  lazy val all_cases = _mm .all_cases

  lazy val per_family_pipeline =
    CcsPerFamilyPipeline .mk (_mm .measure_sum) (_mm .resource_value)

  test ("per family on all outcomes") (
    check (
      obtained = all_cases
        .map ( scenario =>
          per_family_pipeline .apply (scenario) .contents
        )
    ) (
      expected = Seq (true , true , true , false , false , false , false , false)
    )
  )

}


case class CcsSingleGuardianPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  private lazy val _mm = ChildCareSubsidyScenarioExample .mk

  lazy val all_cases = _mm .all_cases

  lazy val single_guardian_pipeline =
    CcsSingleGuardianPipeline .mk (_mm .measure_sum) (_mm .resource_value) (_mm .agent_adults)

  test ("single guardian on all outcomes") (
    check (
      obtained = all_cases
        .map ( scenario =>
          single_guardian_pipeline .apply (scenario) .contents
        )
    ) (
      expected = Seq (true , false , false , true , false , false , false , false)
    )
  )

}


trait ChildCareSubsidyScenarioExample
{



  lazy val resource0 = "no subsidy - 0"

  lazy val resource1 = "subsidy - 100"

  lazy val resource2 = "subsidy - 200"

  lazy val resource3 = "subsidy - 300"

  lazy val agent0 = "family A"

  lazy val agent1 = "family B"

  lazy val agent2 = "family C"

  private def _add_value_to (value : Int) (m : Measure) : Measure =
    m match  {
      case Some (other_value) => Some (value + other_value)
      case None => None
    }

  def measure_sum (a : Measure) (b : Measure) : Measure =
    a match  {
      case Some (value) => _add_value_to (value) (b)
      case None => None
    }

  def get_or_else [A ] (map : Map [A, Measure] ) (key : A) (default : Measure) : Measure =
    map .get (key) match  {
      case Some (value) => value
      case None => default
    }

  lazy val agent_children_map : Map [Agent, Measure] = Seq (
    Tuple2 [Agent, Measure] (agent0 , Some (2) ) ,
    Tuple2 [Agent, Measure] (agent1 , Some (3) ) ,
    Tuple2 [Agent, Measure] (agent2 , Some (1) )
  ) .toMap

  def agent_children (agent : Agent) : Measure =
    get_or_else [Agent] (agent_children_map) (agent) (Some (-1) )

  lazy val agent_adults_map : Map [Agent, Measure] = Seq (
    Tuple2 [Agent, Measure] (agent0 , Some (2) ) ,
    Tuple2 [Agent, Measure] (agent1 , Some (1) ) ,
    Tuple2 [Agent, Measure] (agent2 , Some (2) )
  ) .toMap

  def agent_adults (agent : Agent) : Measure =
    get_or_else [Agent] (agent_adults_map) (agent) (Some (-1) )

  lazy val agent_income_map : Map [Agent, Measure] = Seq (
    Tuple2 [Agent, Measure] (agent0 , Some (5000) ) ,
    Tuple2 [Agent, Measure] (agent1 , Some (3000) ) ,
    Tuple2 [Agent, Measure] (agent2 , Some (800) )
  ) .toMap

  def agent_income (agent : Agent) : Measure =
    get_or_else [Agent] (agent_income_map) (agent) (Some (-1) )

  lazy val resource_value_map : Map [Resource, Measure] = Seq (
    Tuple2 [Resource, Measure] (resource0 , Some (0) ) ,
    Tuple2 [Resource, Measure] (resource1 , Some (100) ) ,
    Tuple2 [Resource, Measure] (resource2 , Some (200) ) ,
    Tuple2 [Resource, Measure] (resource3 , Some (300) )
  ) .toMap

  def resource_value (resource : Resource) : Measure =
    get_or_else [Resource] (resource_value_map) (resource) (Some (-1) )

  lazy val context = "context"

  lazy val outcome_no_subsidy : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource0) ,
        Assignment .mk (agent1) (resource0) ,
        Assignment .mk (agent2) (resource0)
      )
    )

  lazy val initial_no_subsidy : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_no_subsidy) (true)

  lazy val outcome_per_family_0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource1) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource1)
      )
    )

  lazy val initial_per_family_0 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_per_family_0) (true)

  lazy val outcome_per_family_1 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource2) ,
        Assignment .mk (agent2) (resource2)
      )
    )

  lazy val initial_per_family_1 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_per_family_1) (true)

  lazy val outcome_single_guardian : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource0) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource0)
      )
    )

  lazy val initial_single_guardian : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_single_guardian) (true)

  lazy val outcome_per_child : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource3) ,
        Assignment .mk (agent2) (resource1)
      )
    )

  lazy val initial_per_child : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_per_child) (true)

  lazy val outcome_decreasing_on_income_0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource1) ,
        Assignment .mk (agent1) (resource2) ,
        Assignment .mk (agent2) (resource2)
      )
    )

  lazy val initial_decreasing_on_income_0 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_decreasing_on_income_0) (true)

  lazy val outcome_decreasing_on_income_1 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource1) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource3)
      )
    )

  lazy val initial_decreasing_on_income_1 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_decreasing_on_income_1) (true)

  lazy val outcome_decreasing_per_child : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource3) ,
        Assignment .mk (agent2) (resource1)
      )
    )

  lazy val initial_decreasing_per_child : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome_decreasing_per_child) (true)

  lazy val all_cases : Seq [TileMessage [Boolean] ] =
    Seq (
      initial_no_subsidy ,
      initial_per_family_0 ,
      initial_per_family_1 ,
      initial_single_guardian ,
      initial_per_child ,
      initial_decreasing_on_income_0 ,
      initial_decreasing_on_income_1 ,
      initial_decreasing_per_child
    )



}

case class ChildCareSubsidyScenarioExample_ () extends ChildCareSubsidyScenarioExample

object ChildCareSubsidyScenarioExample {
  def mk : ChildCareSubsidyScenarioExample =
    ChildCareSubsidyScenarioExample_ ()
}

