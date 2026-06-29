package soda.tiles.fairness.tile.derived.apply

/*
 * This package contains tests for the primitive tiles.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tile.primitive.ScenarioExample
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder





case class PureTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (value : Int) : TileMessage [Int] =
    TileMessageBuilder
      .mk
      .build (scenario.context) (scenario.outcome0) (value)

  test ("pure puts a single value into a singleton sequence") (
    check(
      obtained = PureTile .mk
        .apply (mk_tile_message(5) )
        .contents
    ) (
      expected = Seq [Int] (5)
    )
  )

  test ("pure preserves the value exactly") (
    check(
      obtained = PureTile .mk
        .apply (mk_tile_message(42) )
        .contents
    ) (
      expected = Seq [Int] (42)
    )
  )

}

