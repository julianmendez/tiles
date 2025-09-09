package soda.tiles.fairness.tile.constant

/*
 * This package contains tests for constant tiles.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tile.primitive.ScenarioExample
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair





case class AllResourceTileSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert(obtained == expected)

  lazy val scenario = ScenarioExample .mk

  lazy val agents = Seq [Agent] ("A0" , "A1" , "A2" , "A3" , "A4" , "A5" , "A6" , "A7")

  def create_outcome (resources : Seq [Resource] ) : Outcome =
    Outcome .mk (
      resources
        .zip (agents)
        .map ( pair => Assignment .mk (pair ._2) (pair ._1) )
    )

  def mk_tile_message (resources : Seq [Resource] ) : TileMessage [Boolean] =
    TileMessageBuilder
      .mk
      .build (scenario .context) (create_outcome (resources) ) (true)

  lazy val all_resource_tile = AllResourceTile .mk

  test ("all resource tile on empty assignments returns empty sequence") (
    check(
      obtained = all_resource_tile
        .apply (mk_tile_message (Seq [Resource] () ) )
        .contents
    ) (
      expected = Seq [Resource] ()
    )
  )

  test ("all resource tile on single resource returns that resource") (
    check(
      obtained = all_resource_tile
        .apply (mk_tile_message (Seq [Resource] ("R1") ) )
        .contents
    ) (
      expected = Seq [Resource] ("R1")
    )
  )

  test ("all resource tile on multiple distinct resources returns sorted sequence") (
    check(
      obtained = all_resource_tile
        .apply (mk_tile_message (Seq [Resource] ("B" , "A" , "C") ) )
        .contents
    ) (
      expected = Seq [Resource] ("A" , "B" , "C")
    )
  )

  test ("all resource tile removes duplicates and sorts") (
    check(
      obtained = all_resource_tile
        .apply (mk_tile_message (Seq [Resource] ("X" , "A" , "X" , "B") ) )
        .contents
    ) (
      expected = Seq [Resource] ("A" , "B" , "X")
    )
  )

}

