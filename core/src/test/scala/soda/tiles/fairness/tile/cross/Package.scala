package soda.tiles.fairness.tile.cross

/*
 * This package contains tests for tiles related to cross.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tile.map.SigmaTile
import   soda.tiles.fairness.tile.scenario.ScenarioExample

case class CrossTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained: A) (expected: A): org.scalatest.compatible.Assertion =
    assert(obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message [A ] (seq: Seq [A] ) : TileMessage [Seq [A] ] =
    TileMessageBuilder
      .mk
      .build(scenario.context)(scenario.outcome0)(seq)

  test ("cross on two empty sequences returns empty sequence") (
    check (
      obtained = CrossTile .mk
        .apply (mk_tile_message (Seq [Int] () ) ) (mk_tile_message (Seq [String] () ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("cross on empty first sequence returns empty sequence") (
    check (
      obtained = CrossTile .mk
        .apply (mk_tile_message (Seq [Int] () ) ) (mk_tile_message (Seq [String] ("a" , "b") ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("cross on empty second sequence returns empty sequence") (
    check (
      obtained = CrossTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2) ) ) (mk_tile_message (Seq [String] () ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("cross on single-element sequences") (
    check (
      obtained = CrossTile .mk
        .apply (mk_tile_message (Seq [Int] (1) ) ) (mk_tile_message (Seq [String] ("a") ) )
        .contents
    ) (
      expected = Seq (TilePair .mk (1) ("a") )
    )
  )

  test ("cross on multi-element sequences") (
    check (
      obtained = CrossTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2) ) ) (mk_tile_message (Seq [String] ("a" , "b") ) )
        .contents
    ) (
      expected = Seq(
        TilePair .mk (1) ("a") ,
        TilePair .mk (1) ("b") ,
        TilePair .mk (2) ("a") ,
        TilePair .mk (2) ("b")
      )
    )
  )

}

