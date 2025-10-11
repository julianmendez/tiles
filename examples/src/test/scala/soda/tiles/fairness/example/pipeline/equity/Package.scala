package soda.tiles.fairness.example.pipeline.equity

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

