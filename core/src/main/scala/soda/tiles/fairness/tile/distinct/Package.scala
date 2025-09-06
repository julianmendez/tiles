package soda.tiles.fairness.tile.distinct

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a collection containing only the unique elements from the original, removing any duplicates while
 * keeping the first occurrence of each.
 */

trait DistinctTile [A ]
{



  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (message .contents) .distinct
    )

}

case class DistinctTile_ [A] () extends DistinctTile [A]

object DistinctTile {
  def mk [A] : DistinctTile [A] =
    DistinctTile_ [A] ()
}

