package soda.tiles.fairness.tile.fold

/*
 * This package contains tests for tiles related to fold.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tile.scenario.ScenarioExample





trait SumFoldTile
  extends
    FoldTile [Int, Int]
{



  lazy val z : Int = 0

  lazy val phi : Int => Int => Int =
     acc =>
       elem =>
        acc + elem

}

case class SumFoldTile_ () extends SumFoldTile

object SumFoldTile {
  def mk : SumFoldTile =
    SumFoldTile_ ()
}

case class FoldTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (seq : Seq [Int] ) : TileMessage [Seq [Int] ] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome0) (seq)

  test ("fold on empty sequence returns initial value") (
    check(
      obtained = SumFoldTile .mk
        .apply (mk_tile_message (Seq [Int] () ) )
        .contents
    ) (
      expected = 0
    )
  )

  test ("fold on single element sequence") (
    check(
      obtained = SumFoldTile .mk
        .apply (mk_tile_message (Seq [Int] (5) ) )
        .contents
    ) (
      expected = 5
    )
  )

  test ("fold on multiple elements sequence") (
    check(
      obtained = SumFoldTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3 , 4) ) )
        .contents
    ) (
      expected = 10
    )
  )

}

