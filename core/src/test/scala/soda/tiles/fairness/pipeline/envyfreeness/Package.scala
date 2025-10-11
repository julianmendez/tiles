package soda.tiles.fairness.pipeline.envyfreeness

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
import   soda.tiles.fairness.pipeline.equity.ResourceAllocationScenarioExample





case class EnvyFreenessPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val ex = ResourceAllocationScenarioExample .mk

  lazy val preference = Preference .mk (
      Seq [Tuple2 [Agent, Seq [Resource] ] ] (
        Tuple2 [Agent, Seq [Resource] ] (ex .agent0 , Seq [Resource] (ex .resource0 , ex .resource1 , ex .resource2) ) ,
        Tuple2 [Agent, Seq [Resource] ] (ex .agent1 , Seq [Resource] (ex .resource1 , ex .resource2 , ex .resource0) ) ,
        Tuple2 [Agent, Seq [Resource] ] (ex .agent2 , Seq [Resource] (ex .resource2 , ex .resource0 , ex .resource1) )
      ) .toMap
    )

  lazy val envy_freeness_pipeline = EnvyFreenessPipeline .mk (preference)

  test ("envy freeness on outcome 0") (
    check (
      obtained = envy_freeness_pipeline .apply (ex .initial0) .contents
    ) (
      expected = true
    )
  )

  test ("envy freeness on outcome 1") (
    check (
      obtained = envy_freeness_pipeline .apply (ex .initial1) .contents
    ) (
      expected = false
    )
  )

  test ("envy freeness on outcome 2") (
    check (
      obtained = envy_freeness_pipeline .apply (ex .initial2) .contents
    ) (
      expected = false
    )
  )

  test ("envy freeness on outcome 3") (
    check (
      obtained = envy_freeness_pipeline .apply (ex .initial3) .contents
    ) (
      expected = false
    )
  )

}

