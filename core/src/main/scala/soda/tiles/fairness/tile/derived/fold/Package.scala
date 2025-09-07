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





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This takes a sequence, a starting value, and a function, then processes the sequence from left to right,
 * combining elements into a single result step by step.
 */

trait SumPhiTile [A ]
{

  def   phi : A => Measure

  lazy val measure_zero : Measure = Some (0)

  def combine (acc : Measure) (elem : A) : Measure =
    MeasureMod .mk .plus (acc) (phi (elem) )

  lazy val fold_tile = FoldTile .mk (measure_zero) (combine)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Measure] =
    fold_tile
      .apply (message)

}

case class SumPhiTile_ [A] (phi : A => Measure) extends SumPhiTile [A]

object SumPhiTile {
  def mk [A] (phi : A => Measure) : SumPhiTile [A] =
    SumPhiTile_ [A] (phi)
}

