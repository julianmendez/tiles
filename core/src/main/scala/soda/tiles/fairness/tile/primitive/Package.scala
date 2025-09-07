package soda.tiles.fairness.tile.primitive

/*
 * This package contains classes to model the primitive tiles.
 */

import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This takes a transformation function and applies it to a single element,
 * producing a TileMessage with the transformed element.
 */

trait ApplyTile [A , B ]
{

  def   phi : A => B

  def apply (message : TileMessage [A] ) : TileMessage [B] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      phi (message .contents)
    )

}

case class ApplyTile_ [A, B] (phi : A => B) extends ApplyTile [A, B]

object ApplyTile {
  def mk [A, B] (phi : A => B) : ApplyTile [A, B] =
    ApplyTile_ [A, B] (phi)
}


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
          .map ( b => TilePair .mk [A, B] (a) (b) )
      )

  def apply [A , B ] (message0 : TileMessage [Seq [A] ] ) (message1 : TileMessage [Seq [B] ] )
      : TileMessage [Seq [TilePair [A, B] ] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      cross_lists (message0 .contents) (message1 .contents)
    )

}

case class CrossTile_ () extends CrossTile

object CrossTile {
  def mk : CrossTile =
    CrossTile_ ()
}


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


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This takes a sequence, a starting value, and a function, then processes the sequence from left to right,
 * combining elements into a single result step by step.
 */

trait FoldTile [A , B ]
{

  def   z : B
  def   phi : B => A => B

  private def _tailrec_foldl (sequence : Seq [A] ) (current : B) (next : B => A => B) : B =
    sequence match  {
      case Nil => current
      case (head) +: (tail) =>
        _tailrec_foldl (tail) (next (current) (head) ) (next)
    }

  def foldl (sequence : Seq [A] ) (initial : B) (next : B => A => B) : B =
    _tailrec_foldl (sequence) (initial) (next)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [B] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (foldl (message .contents) (z) (phi) )
    )

}

case class FoldTile_ [A, B] (z : B, phi : B => A => B) extends FoldTile [A, B]

object FoldTile {
  def mk [A, B] (z : B) (phi : B => A => B) : FoldTile [A, B] =
    FoldTile_ [A, B] (z, phi)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This takes a transformation function and applies it to each element of the sequence,
 * producing a new sequence with the transformed elements, preserving the original order.
 */

trait MapTile [A , B ]
{

  def   phi : A => B

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [B] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (message .contents) .map (phi)
    )

}

case class MapTile_ [A, B] (phi : A => B) extends MapTile [A, B]

object MapTile {
  def mk [A, B] (phi : A => B) : MapTile [A, B] =
    MapTile_ [A, B] (phi)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects two sequences and returns a sequence of pairs, such that for each
 * position in both sequences, it has a pair with elements for the corresponding input
 * sequences.
 */

trait ZipTile
{



  def zip_lists [A , B ] (list0 : Seq [A] ) (list1 : Seq [B] )
      : Seq [TilePair [A, B] ] =
    list0
      .zip (list1)
      .map ( pair => TilePair .mk [A, B] (pair ._1) (pair ._2) )

  def apply [A , B ] (message0 : TileMessage [Seq [A] ] )
      (message1 : TileMessage [Seq [B] ] ) : TileMessage [Seq [TilePair [A, B] ] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      zip_lists (message0 .contents) (message1 .contents)
    )

}

case class ZipTile_ () extends ZipTile

object ZipTile {
  def mk : ZipTile =
    ZipTile_ ()
}

