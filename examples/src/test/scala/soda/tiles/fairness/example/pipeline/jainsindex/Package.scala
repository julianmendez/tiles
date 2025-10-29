package soda.tiles.fairness.example.pipeline.equity

/*
 * This package contains tests for the classes to model Jain's index.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.example.pipeline.jainsindex.JainsIndexPipeline





case class JainsIndexPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val example = JainsIndexScenarioExample .mk

  lazy val jains_index_pipeline =
    JainsIndexPipeline .mk (example .resource_utility)

  test ("perfectly equal allocation gives Jain's index = 1") (
    check (
      obtained = jains_index_pipeline .runner (example .initial0) .contents
    ) (
      expected = 1.0
    )
  )

  /** Jain's index = (20^2) / (2 * (20^2 + 0^2)) = 400 / 800 = 0.5 */

  test ("unequal allocation gives Jain's index < 1") (
    check (
      obtained = jains_index_pipeline .runner (example .initial1) .contents
    ) (
      expected = 0.5
    )
  )

  /** Jain's index = (30^2) / (3 * (25 + 100 + 225)) = 900 / (3*350) = 900 / 1050 ≈ 0.857 */

  test ("three agents with different allocations") (
    check (
      obtained = (jains_index_pipeline .runner (example .initial2) .contents * 1000) .intValue
    ) (
      expected = 857
    )
  )

}


/**
 * This is a resource allocation scenario. There are three people of different heights. The may
 * need boxes to stand on them and to watch above a wall.
 */

trait JainsIndexScenarioExample
{



  lazy val resource0 = "R0"

  lazy val resource5 = "R5"

  lazy val resource10 = "R10"

  lazy val resource15 = "R15"

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
    Tuple2 [Resource, Measure] (resource0 , Some (0) ) ,
    Tuple2 [Resource, Measure] (resource5 , Some (5) ) ,
    Tuple2 [Resource, Measure] (resource10 , Some (10) ) ,
    Tuple2 [Resource, Measure] (resource15 , Some (15) ) ,
    Tuple2 [Resource, Measure] (resource20 , Some (20) ) ,
    Tuple2 [Resource, Measure] (resource50 , Some (50) ) ,
    Tuple2 [Resource, Measure] (resource100 , Some (100) )
  ) .toMap

  def resource_utility (resource : Resource) : Measure =
    resource_utility_map .getOrElse (resource , Some (-1) )

  lazy val context = "context"

  lazy val outcome0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource10) ,
        Assignment .mk (agent1) (resource10)
      )
    )

  lazy val outcome1 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource20) ,
        Assignment .mk (agent1) (resource0)
      )
    )

  lazy val outcome2 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource5) ,
        Assignment .mk (agent1) (resource10) ,
        Assignment .mk (agent2) (resource15)
      )
    )

  lazy val initial0 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome0) (true)

  lazy val initial1 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome1) (true)

  lazy val initial2 : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome2) (true)

}

case class JainsIndexScenarioExample_ () extends JainsIndexScenarioExample

object JainsIndexScenarioExample {
  def mk : JainsIndexScenarioExample =
    JainsIndexScenarioExample_ ()
}

