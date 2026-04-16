package soda.tiles.fairness.example.pipeline.complementtheilindex

/*
 * This package contains tests for the classes to model the complement of the Theil index.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.example.pipeline.complementtheilindex.ComplementTheilIndexPipeline
import   soda.tiles.fairness.example.pipeline.equity.JainsIndexScenarioExample





case class ComplementTheilIndexPipelineSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val example = JainsIndexScenarioExample .mk

  lazy val complement_theil_index_pipeline =
    ComplementTheilIndexPipeline .mk (example .resource_utility)

  test ("perfectly equal allocation gives complement Theil index = 1") (
    check (
      obtained = complement_theil_index_pipeline .runner (example .initial0) .contents
    ) (
      expected = 1.0
    )
  )

  /** For allocation (20, 1), Theil index ≈ 0.501, so complement ≈ 0.498 */

  test ("unequal allocation gives complement Theil index < 1") (
    check (
      obtained = (complement_theil_index_pipeline .runner (example .initial1) .contents * 1000) .intValue
    ) (
      expected = 498
    )
  )

  /** For allocation (5, 10, 15), Theil index ≈ 0.087, so complement ≈ 0.912 */

  test ("three agents with different allocations") (
    check (
      obtained = (complement_theil_index_pipeline .runner (example .initial2) .contents * 1000) .intValue
    ) (
      expected = 912
    )
  )

}

