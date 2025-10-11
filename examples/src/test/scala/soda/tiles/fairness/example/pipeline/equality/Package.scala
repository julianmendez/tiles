package soda.tiles.fairness.example.pipeline.equality

/*
 * This package contains tests for the classes to model a resource allocation scenario.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.example.pipeline.equity.ResourceAllocationScenarioExample





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

