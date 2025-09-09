package soda.tiles.fairness.tile.derived.fold

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.primitive.FoldTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.MeasureMod
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





/**
 * This computes the length of a sequence.
 */

trait LengthTile [A ]
{



  lazy val one : Measure = MeasureMod .mk .one

  def phi (x : A) : Measure =
    one

  lazy val sum_phi_tile = SumPhiTile .mk [A] (phi)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Measure] =
    sum_phi_tile .apply (
      message
    )

}

case class LengthTile_ [A] () extends LengthTile [A]

object LengthTile {
  def mk [A] : LengthTile [A] =
    LengthTile_ [A] ()
}


/**
 * This processes a sequence of measures and returns a pair, which has the sum on the first component and
 * the number of elements on the second component.
 */

trait SumCountTile
{



  lazy val zero : Measure = MeasureMod .mk .zero

  lazy val one : Measure = MeasureMod .mk .one

  lazy val pair_zero : TilePair [Measure, Measure] =
    TilePair .mk (zero) (zero)

  def combine (acc : TilePair [Measure, Measure] ) (elem : Measure)
      : TilePair [Measure, Measure] =
    TilePair .mk (MeasureMod .mk .plus (acc .fst) (elem) ) (MeasureMod .mk .plus (acc .snd) (one) )

  lazy val fold_tile = FoldTile .mk (pair_zero) (combine)

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [TilePair [Measure, Measure] ] =
    fold_tile .apply (message)

}

case class SumCountTile_ () extends SumCountTile

object SumCountTile {
  def mk : SumCountTile =
    SumCountTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This takes a sequence, a starting value, and a function, then processes the sequence from left to right,
 * adding elements into a single result step by step.
 */

trait SumPhiTile [A ]
{

  def   phi : A => Measure

  lazy val zero : Measure = MeasureMod .mk .zero

  def combine (acc : Measure) (elem : A) : Measure =
    MeasureMod .mk .plus (acc) (phi (elem) )

  lazy val fold_tile = FoldTile .mk (zero) (combine)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Measure] =
    fold_tile
      .apply (message)

}

case class SumPhiTile_ [A] (phi : A => Measure) extends SumPhiTile [A]

object SumPhiTile {
  def mk [A] (phi : A => Measure) : SumPhiTile [A] =
    SumPhiTile_ [A] (phi)
}


/**
 * This computes the sum of the elements of a sequence.
 */

trait SumTile [A ]
{



  def phi (x : Measure) : Measure =
    x

  lazy val sum_phi_tile = SumPhiTile .mk [Measure] (phi)

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Measure] =
    sum_phi_tile .apply (
      message
    )

}

case class SumTile_ [A] () extends SumTile [A]

object SumTile {
  def mk [A] : SumTile [A] =
    SumTile_ [A] ()
}

