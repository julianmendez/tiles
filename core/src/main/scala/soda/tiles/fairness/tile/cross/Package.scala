package soda.tiles.fairness.tile.cross

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tile.map.SigmaTile





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects two sequences and returns a sequence of pairs,
 * such that every element of the first sequence is paired with every
 * element of the second sequence (Cartesian product).
 */

trait CrossTile
{



  def cross_lists [A , B ] (list0 : Seq [A] ) (list1 : Seq [B] )
      : Seq [TilePair [A, B] ] =
    list0
      .flatMap ( a =>
        list1
          .map ( b => TilePair.mk [A, B] (a) (b) )
      )

  def apply [A , B ] (message0 : TileMessage [Seq [A] ] ) (message1 : TileMessage [Seq [B] ] )
      : TileMessage [Seq [TilePair [A, B] ] ] =
    TileMessageBuilder.mk.build (message0.context) (message0.outcome) (
      cross_lists (message0.contents) (message1.contents)
    )

}

case class CrossTile_ () extends CrossTile

object CrossTile {
  def mk : CrossTile =
    CrossTile_ ()
}

