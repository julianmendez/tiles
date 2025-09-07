package soda.tiles.fairness.tile.derived.fold

/*
 * This package contains tests for derived tiles.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tile.primitive.ScenarioExample
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair





case class SumCountTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert(obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (seq : Seq [Int] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome0) (seq .map ( x => Some (x) ) )

  lazy val sum_count_tile = SumCountTile .mk

  test ("sum and count on empty sequence") (
    check(
      obtained = sum_count_tile
        .apply (mk_tile_message (Seq [Int] () ) )
        .contents
    ) (
      expected = TilePair .mk (Some (0) ) (Some (0) )
    )
  )

  test ("sum and count on single element sequence") (
    check (
      obtained = sum_count_tile
        .apply (mk_tile_message (Seq [Int] (5) ) )
        .contents
    ) (
      expected = TilePair .mk (Some (5) ) (Some (1) )
    )
  )

  test ("sum and count on multiple elements sequence") (
    check (
      obtained = sum_count_tile
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3 , 4) ) )
        .contents
    ) (
      expected = TilePair .mk (Some(10) ) (Some(4) )
    )
  )

}


case class SumPhiTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert(obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (seq : Seq [Int] ) : TileMessage [Seq [Int] ] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome0) (seq)

  def phi (a : Int) : Measure =
    Some (a)

  lazy val sum_phi_tile = SumPhiTile .mk [Int] (phi)

  test ("sum phi on empty sequence returns measure zero") (
    check (
      obtained = sum_phi_tile
        .apply (mk_tile_message(Seq [Int] () ) )
        .contents
    ) (
      expected = Some(0)
    )
  )

  test ("sum phi on single element sequence") (
    check(
      obtained = sum_phi_tile
        .apply (mk_tile_message (Seq [Int] (5) ) )
        .contents
    ) (
      expected = Some(5)
    )
  )

  test ("sum phi on multiple elements sequence") (
    check(
      obtained = sum_phi_tile
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3 , 4) ) )
        .contents
    ) (
      expected = Some(10)
    )
  )

}

