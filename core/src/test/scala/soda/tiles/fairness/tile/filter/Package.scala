package soda.tiles.fairness.tile.filter

/*
 * This package contains tests for tiles related to filter.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tile.scenario.ScenarioExample





trait EvenFilterTile
  extends
    FilterTile [Int]
{



  lazy val phi : Int => Boolean =
     elem => (elem % 2 == 0)

}

case class EvenFilterTile_ () extends EvenFilterTile

object EvenFilterTile {
  def mk : EvenFilterTile =
    EvenFilterTile_ ()
}

case class FilterTileSpec ()
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

  test ("filter on empty sequence returns empty sequence") (
    check(
      obtained = EvenFilterTile .mk
        .apply (mk_tile_message (Seq [Int] () ) )
        .contents
    ) (
      expected = Seq [Int] ()
    )
  )

  test ("filter on sequence with no matching elements") (
    check(
      obtained = EvenFilterTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 3 , 5) ) )
        .contents
    ) (
      expected = Seq [Int] ()
    )
  )

  test ("filter on sequence with some matching elements") (
    check(
      obtained = EvenFilterTile .mk
        .apply (mk_tile_message (Seq [Int] (1 , 2 , 3 , 4 , 5) ) )
        .contents
    ) (
      expected = Seq [Int] (2 , 4)
    )
  )

  test ("filter on sequence with all matching elements") (
    check(
      obtained = EvenFilterTile .mk
        .apply (mk_tile_message (Seq [Int] (2 , 4 , 6) ) )
        .contents
    ) (
      expected = Seq [Int] (2 , 4 , 6)
    )
  )

}

