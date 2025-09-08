package soda.tiles.fairness.tile.primitive

/*
 * This package contains tests for the primitive tiles.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair

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
      obtained = CrossTile .mk [Int, String]
        .apply (mk_tile_message (Seq [Int] () ) ) (mk_tile_message (Seq [String] () ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("cross on empty first sequence returns empty sequence") (
    check (
      obtained = CrossTile .mk [Int, String]
        .apply (mk_tile_message (Seq [Int] () ) ) (mk_tile_message (Seq [String] ("a" , "b") ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("cross on empty second sequence returns empty sequence") (
    check (
      obtained = CrossTile .mk [Int, String]
        .apply (mk_tile_message (Seq [Int] (1 , 2) ) ) (mk_tile_message (Seq [String] () ) )
        .contents
    ) (
      expected = Seq [TilePair [Int, String] ] ()
    )
  )

  test ("cross on single-element sequences") (
    check (
      obtained = CrossTile .mk [Int, String]
        .apply (mk_tile_message (Seq [Int] (1) ) ) (mk_tile_message (Seq [String] ("a") ) )
        .contents
    ) (
      expected = Seq (TilePair .mk (1) ("a") )
    )
  )

  test ("cross on multi-element sequences") (
    check (
      obtained = CrossTile .mk [Int, String]
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


trait ScenarioExample
{



  lazy val context = "context"

  lazy val resource0 = "small box - 0.1 m"

  lazy val resource1 = "medium box - 0.2 m"

  lazy val resource2 = "large box - 0.3 m"

  lazy val resource3 = "10"

  lazy val resource4 = "20"

  lazy val resource5 = "30"

  lazy val resource6 = "40"

  lazy val agent0 = "Anna A"

  lazy val agent1 = "Bob B"

  lazy val agent2 = "Charlie C"

  lazy val agent3 = "David D"

  lazy val agent4 = "Eva E"

  lazy val outcome0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource0)
      )
    )

  lazy val outcome1 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource3) ,
        Assignment .mk (agent1) (resource4) ,
        Assignment .mk (agent2) (resource5) ,
        Assignment .mk (agent0) (resource6)
      )
    )

}

case class ScenarioExample_ () extends ScenarioExample

object ScenarioExample {
  def mk : ScenarioExample =
    ScenarioExample_ ()
}


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

