package soda.tiles.fairness.tile.derived.bind

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.primitive.BindTile
import   soda.tiles.fairness.tool.TileMessage





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

  def filter (elem : A) : Seq [A] =
    if ( (phi (elem) )
    ) Seq [A] (elem)
    else Seq [A] ()

  lazy val bind_tile = BindTile .mk (filter)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [A] ] =
    bind_tile .apply (
      message
    )

}

case class FilterTile_ [A] (phi : A => Boolean) extends FilterTile [A]

object FilterTile {
  def mk [A] (phi : A => Boolean) : FilterTile [A] =
    FilterTile_ [A] (phi)
}

