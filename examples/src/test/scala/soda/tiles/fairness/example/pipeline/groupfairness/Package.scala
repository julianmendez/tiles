package soda.tiles.fairness.example.pipeline.groupfairness

/*
 * This package contains tests for the classes that model group fairness.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder





case class GroupFairnessPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val ex = GroupFairnessScenarioExample .mk

  lazy val group_fairness_pipeline = GroupFairnessPipeline .mk (ex .protected_attribute)

  test ("group fairness on outcome 0") (
    check (
      obtained = group_fairness_pipeline .apply (ex .initial0) .contents
    ) (
      expected = true
    )
  )

  test ("group fairness on outcome 1") (
    check (
      obtained = group_fairness_pipeline .apply (ex .initial1) .contents
    ) (
      expected = true
    )
  )

  test ("group fairness on outcome 2") (
    check (
      obtained = group_fairness_pipeline .apply (ex .initial2) .contents
    ) (
      expected = false
    )
  )

}


trait GroupFairnessScenarioExample
{



  lazy val resource20 = "R20"

  lazy val resource50 = "R50"

  lazy val resource100 = "R100"

  lazy val agent0 = "A"

  lazy val agent1 = "B"

  lazy val agent2 = "C"

  lazy val agent3 = "D"

  lazy val agent4 = "E"

  lazy val agent5 = "F"

  lazy val resource_utility_map : Map [Resource, Measure] = Seq (
    Tuple2 [Resource, Measure] (resource20 , Some (20) ) ,
    Tuple2 [Resource, Measure] (resource50 , Some (50) ) ,
    Tuple2 [Resource, Measure] (resource100 , Some (100) )
  ) .toMap

  def resource_utility (resource : Resource) : Measure =
    resource_utility_map .getOrElse (resource , Some (-1) )

  lazy val protected_attribute_set : Set [Agent] =
    Set [Agent] (
      agent0 , agent2 , agent4
    )

  def protected_attribute (agent : Agent) : Boolean =
    protected_attribute_set .contains (agent)

  lazy val context = "context"

  lazy val outcome0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource20) ,
        Assignment .mk (agent1) (resource20) ,
        Assignment .mk (agent2) (resource20) ,
        Assignment .mk (agent3) (resource20) ,
        Assignment .mk (agent4) (resource20) ,
        Assignment .mk (agent5) (resource20)
      )
    )

  lazy val outcome1 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource50) ,
        Assignment .mk (agent5) (resource50)
      )
    )

  lazy val outcome2 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent1) (resource50) ,
        Assignment .mk (agent3) (resource50) ,
        Assignment .mk (agent5) (resource50)
      )
    )

  lazy val initial0 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome0) (true)

  lazy val initial1 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome1) (true)

  lazy val initial2 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome2) (true)

}

case class GroupFairnessScenarioExample_ () extends GroupFairnessScenarioExample

object GroupFairnessScenarioExample {
  def mk : GroupFairnessScenarioExample =
    GroupFairnessScenarioExample_ ()
}

