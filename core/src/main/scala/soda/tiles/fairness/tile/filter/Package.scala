package soda.tiles.fairness.tile.filter

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tile.zip.ZipPairTile





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This takes a condition (predicate) and passes through only those elements that satisfy it, discarding all others
 * while preserving the original order.
 */

trait FilterTile [A ]
{

  def   phi : A => Boolean

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (message .contents) .filter (phi)
    )

}

case class FilterTile_ [A] (phi : A => Boolean) extends FilterTile [A]

object FilterTile {
  def mk [A] (phi : A => Boolean) : FilterTile [A] =
    FilterTile_ [A] (phi)
}

