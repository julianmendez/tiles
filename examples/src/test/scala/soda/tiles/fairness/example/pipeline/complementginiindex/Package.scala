package soda.tiles.fairness.example.pipeline.equity

/*
 * This package contains tests for the classes to model the complement of the Gini index.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.example.pipeline.complementginiindex.ComplementGiniIndexPipeline





case class ComplementGiniIndexPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val example = JainsIndexScenarioExample .mk

  lazy val complement_gini_index_pipeline =
    ComplementGiniIndexPipeline .mk (example .resource_utility)

  test ("perfectly equal allocation gives complement Gini index = 1") (
    check (
      obtained = complement_gini_index_pipeline .runner (example .initial0) .contents
    ) (
      expected = 1.0
    )
  )

  /** For allocation (20, 0), Gini index = 0.5, so complement = 0.5 */

  test ("unequal allocation gives complement Gini index < 1") (
    check (
      obtained = complement_gini_index_pipeline .runner (example .initial1) .contents
    ) (
      expected = 0.5
    )
  )

  /** For allocation (5, 10, 15), Gini index ≈ 0.222, so complement ≈ 0.777 */

  test ("three agents with different allocations") (
    check (
      obtained = (complement_gini_index_pipeline .runner (example .initial2) .contents * 1000) .intValue
    ) (
      expected = 777
    )
  )

}

