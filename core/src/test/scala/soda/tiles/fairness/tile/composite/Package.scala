package soda.tiles.fairness.tile.composite

/*
 * This package contains tests for derived tiles.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tile.primitive.ScenarioExample
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.OutcomeMod
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair





case class AccumulatesTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (seq : Seq [Agent] ) : TileMessage [Seq [Agent] ] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome1) (seq)

  def utility (resource : Resource) : Measure =
    Some (resource .toIntOption .getOrElse (resource .length) )

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  test ("accumulates on empty sequence returns empty sequence") (
    check(
      obtained = accumulates_tile
        .apply (mk_tile_message (Seq [Agent] () ) )
        .contents
    ) (
      expected = Seq [Measure] ()
    )
  )

  test ("accumulates on single agent with no resources") (
    check(
      obtained = accumulates_tile
        .apply (mk_tile_message (Seq [Agent] (scenario .agent3) ) )
        .contents
    ) (
      expected = Seq [Measure] (Some (0) )
    )
  )

  test ("accumulates on multiple agents with resources") (
    check(
      obtained = accumulates_tile
        .apply (mk_tile_message (Seq [Agent] (scenario .agent0 , scenario .agent1) ) )
        .contents
    ) (
      expected = Seq [Measure] (
        Some (50),
        Some (20)
      )
    )
  )

}


case class AverageTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  lazy val tile = AverageTile .mk

  lazy val scenario = ScenarioExample .mk

  def mk_tile_message (seq: Seq [Measure] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (scenario .outcome0) (seq)

  test ("average of empty sequence should be zero") (
    check (
      obtained = tile
        .apply (mk_tile_message (Seq [Measure] () ) )
        .contents
    ) (
      expected = Some (0)
    )
  )

  test ("average of single element sequence should be that element") (
    check (
      obtained = tile
        .apply (mk_tile_message (Seq (Some (5) ) ) )
        .contents
    ) (
      expected = Some (5)
    )
  )

  test ("average of multiple elements should be correct") (
    check (
      obtained = tile
        .apply (mk_tile_message (Seq (Some (2) , Some (4) , Some (6) ) ) )
        .contents
    ) (
      expected = Some (4)
    )
  )

  test ("average should return None if any element is None") (
    check (
      obtained = tile
        .apply (mk_tile_message (Seq (Some (2) , None , Some (6) ) ) )
        .contents
    ) (
      expected = None
    )
  )

  test ("average of sequence with zero elements should be zero") (
    check (
      obtained = tile .apply_function_with (Some (10) ) (Some (0) )
    ) (
      expected = Some (0)
    )
  )

}

