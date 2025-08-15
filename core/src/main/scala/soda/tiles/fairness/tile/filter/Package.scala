package soda.tiles.fairness.tile.filter

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Pearson
import   soda.tiles.fairness.tool.PearsonMod
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TilePair_
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tool.TileTriple_
import   soda.tiles.fairness.tool.Number





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a possibly empty sequence of agents that satisfy a given property.
 */

trait FilterTile [A ]
{

  def   p : A => Boolean

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .filter ( elem => p (elem) ) )
    )

}

case class FilterTile_ [A] (p : A => Boolean) extends FilterTile [A]

object FilterTile {
  def mk [A] (p : A => Boolean) : FilterTile [A] =
    FilterTile_ [A] (p)
}

