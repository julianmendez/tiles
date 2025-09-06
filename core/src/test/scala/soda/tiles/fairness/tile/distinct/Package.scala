package soda.tiles.fairness.tile.distinct

/*
 * This package contains tests for tiles related to distinct.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tile.scenario.ScenarioExample





case class DistinctTileSpec ()
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

  test ("distinct on empty sequence returns empty sequence") (
    check(
      obtained = DistinctTile .mk
        .apply (mk_tile_message (Seq [Int] () ) )
        .contents
    ) (
      expected = Seq [Int] ()
    )
  )

  test ("distinct on sequence with no duplicates") (
    check(
      obtained = DistinctTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3) ) )
        .contents
    ) (
      expected = Seq [Int] (1 , 2 , 3)
    )
  )

  test ("distinct on sequence with some duplicates") (
    check(
      obtained = DistinctTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 2 , 3 , 1 , 4) ) )
        .contents
    ) (
      expected = Seq [Int] (1 , 2 , 3 , 4)
    )
  )

  test ("distinct on sequence with all duplicates") (
    check(
      obtained = DistinctTile .mk
        .apply (mk_tile_message (Seq [Int] (5 , 5 , 5 , 5) ) )
        .contents
    ) (
      expected = Seq [Int] (5)
    )
  )

}

