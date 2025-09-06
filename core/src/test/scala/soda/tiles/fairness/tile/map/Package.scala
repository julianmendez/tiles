package soda.tiles.fairness.tile.map

/*
 * This package contains tests for tiles related to fold.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tile.scenario.ScenarioExample





trait DoubleMapTile
  extends
    MapTile [Int, Int]
{



  lazy val phi : Int => Int =
     elem => elem * 2

}

case class DoubleMapTile_ () extends DoubleMapTile

object DoubleMapTile {
  def mk : DoubleMapTile =
    DoubleMapTile_ ()
}

case class MapTileSpec ()
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

  test ("map on empty sequence returns empty sequence") (
    check(
      obtained = DoubleMapTile .mk
        .apply (mk_tile_message (Seq [Int] () ) )
        .contents
    ) (
      expected = Seq [Int] ()
    )
  )

  test ("map on single element sequence") (
    check(
      obtained = DoubleMapTile .mk
        .apply (mk_tile_message (Seq [Int] (5) ) )
        .contents
    ) (
      expected = Seq [Int] (10)
    )
  )

  test ("map on multiple elements sequence") (
    check(
      obtained = DoubleMapTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3 , 4) ) )
        .contents
    ) (
      expected = Seq [Int] (2 , 4 , 6 , 8)
    )
  )

}

