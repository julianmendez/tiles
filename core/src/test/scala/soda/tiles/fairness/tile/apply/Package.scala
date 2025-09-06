package soda.tiles.fairness.tile.apply

/*
 * This package contains tests for tiles related to apply.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tile.scenario.ScenarioExample





trait SquareApplyTile
  extends
    ApplyTile [Int, Int]
{



  lazy val phi : Int => Int =
     elem => elem * elem

}

case class SquareApplyTile_ () extends SquareApplyTile

object SquareApplyTile {
  def mk : SquareApplyTile =
    SquareApplyTile_ ()
}

case class ApplyTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (value : Int) : TileMessage [Int] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome0) (value)

  test ("apply on single value") (
    check(
      obtained = SquareApplyTile .mk
        .apply (mk_tile_message (5) )
        .contents
    ) (
      expected = 25
    )
  )

  test ("apply on zero") (
    check(
      obtained = SquareApplyTile .mk
        .apply (mk_tile_message (0) )
        .contents
    ) (
      expected = 0
    )
  )

  test ("apply on negative value") (
    check(
      obtained = SquareApplyTile .mk
        .apply (mk_tile_message (-3) )
        .contents
    ) (
      expected = 9
    )
  )

}

