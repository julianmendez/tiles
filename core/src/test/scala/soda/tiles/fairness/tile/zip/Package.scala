package soda.tiles.fairness.tile.zip

/*
 * This package contains tests for tiles related to zip.
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

case class ZipTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message [T ] (seq : Seq [T] ) : TileMessage [Seq [T] ] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome0) (seq)

  test ("zip on two empty sequences returns empty sequence") (
    check (
      obtained = ZipTile .mk
        .apply (mk_tile_message (Seq [Int] () ) ) (mk_tile_message (Seq [String] () ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("zip on sequences of equal length") (
    check (
      obtained = ZipTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3) ) ) (mk_tile_message (Seq [String] ("a" , "b" , "c") ) )
        .contents
    ) (
      expected = Seq (
        TilePair .mk [Int, String] (1) ("a") ,
        TilePair .mk [Int, String] (2) ("b") ,
        TilePair .mk [Int, String] (3) ("c")
      )
    )
  )

  test ("zip truncates to the shorter sequence (first shorter)") (
    check (
      obtained = ZipTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2) ) ) (mk_tile_message (Seq [String] ("a" , "b" , "c") ) )
        .contents
    ) (
      expected = Seq (
        TilePair .mk [Int, String] (1) ("a") ,
        TilePair .mk [Int, String] (2) ("b")
      )
    )
  )

  test ("zip truncates to the shorter sequence (second shorter)") (
    check (
      obtained = ZipTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3) ) ) (mk_tile_message (Seq [String] ("a") ) )
        .contents
    ) (
      expected = Seq (
        TilePair .mk [Int, String] (1) ("a")
      )
    )
  )

}

