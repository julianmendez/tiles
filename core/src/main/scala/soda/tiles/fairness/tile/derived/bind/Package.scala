package soda.tiles.fairness.tile.derived.bind

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.primitive.BindTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





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


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile applies a transformation function to each element of the sequence,
 * producing a new sequence with the transformed elements.
 */

trait MapTile [A , B ]
{

  def   phi : A => B

  def bind_phi (x : A) : Seq [B] =
    Seq [B] (phi (x) )

  lazy val bind_tile = BindTile .mk (bind_phi)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [B] ] =
    bind_tile .apply (
      message
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
import soda.tiles.fairness.tile.derived.bind.MapTile
*/

/**
 * This tile is a particular case of an 'MapTile', where the attribute is 'needed'.
 */

trait NeedsTile
{

  def   q : Agent => Measure

  lazy val map_tile = MapTile .mk [Agent, Measure] (q)

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    map_tile .apply (
      message
    )

}

case class NeedsTile_ (q : Agent => Measure) extends NeedsTile

object NeedsTile {
  def mk (q : Agent => Measure) : NeedsTile =
    NeedsTile_ (q)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pairs of measures as input, and returns a sequence such that,
 * for each pair (m0, m1) in the input, is m = sigma (m0, m1), where sigma is a given function
 * to combine measures.
 */

trait SigmaTile
{

  def   sigma : Measure => Measure => Measure

  def combine (pair : TilePair [Measure, Measure] ) : Measure =
    sigma (pair .fst) (pair .snd)

  lazy val map_tile = MapTile .mk [TilePair [Measure, Measure] , Measure] (combine)

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] ) : TileMessage [Seq [Measure] ] =
    map_tile .apply (
      message
    )

}

case class SigmaTile_ (sigma : Measure => Measure => Measure) extends SigmaTile

object SigmaTile {
  def mk (sigma : Measure => Measure => Measure) : SigmaTile =
    SigmaTile_ (sigma)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pairs (a, b), and returns a sequence with the first
 * component of each pair from the input.
 */

trait UnzipPairFstTile [A , B ]
{



  def get_fst (pair : TilePair [A, B] ) : A =
    pair .fst

  lazy val map_tile = MapTile .mk [TilePair [A, B] , A] (get_fst)

  def apply (message : TileMessage [Seq [TilePair [A, B] ] ] ) : TileMessage [Seq [A] ] =
    map_tile .apply (
      message
    )

}

case class UnzipPairFstTile_ [A, B] () extends UnzipPairFstTile [A, B]

object UnzipPairFstTile {
  def mk [A, B] : UnzipPairFstTile [A, B] =
    UnzipPairFstTile_ [A, B] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pairs (a, b), and returns a sequence with the second
 * component of each pair from the input.
 */

trait UnzipPairSndTile [A , B ]
{



  def get_snd (pair : TilePair [A, B] ) : B =
    pair .snd

  lazy val map_tile = MapTile .mk [TilePair [A, B] , B] (get_snd)

  def apply (message : TileMessage [Seq [TilePair [A, B] ] ] ) : TileMessage [Seq [B] ] =
    map_tile .apply (
      message
    )

}

case class UnzipPairSndTile_ [A, B] () extends UnzipPairSndTile [A, B]

object UnzipPairSndTile {
  def mk [A, B] : UnzipPairSndTile [A, B] =
    UnzipPairSndTile_ [A, B] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of triples (a, b, c), and returns a sequence with the first
 * component of each triple from the input.
 */

trait UnzipTripleFstTile [A , B , C ]
{



  def get_fst  (triple : TileTriple [A, B, C] ) : A =
    triple .fst

  lazy val map_tile = MapTile .mk [TileTriple [A, B, C] , A] (get_fst)

  def apply (message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [A] ] =
    map_tile .apply (
      message
    )

}

case class UnzipTripleFstTile_ [A, B, C] () extends UnzipTripleFstTile [A, B, C]

object UnzipTripleFstTile {
  def mk [A, B, C] : UnzipTripleFstTile [A, B, C] =
    UnzipTripleFstTile_ [A, B, C] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of triples (a, b, c), and returns a sequence with the second
 * component of each triple from the input.
 */

trait UnzipTripleSndTile [A , B , C ]
{



  def get_snd (triple : TileTriple [A, B, C] ) : B =
    triple .snd

  lazy val map_tile = MapTile .mk [TileTriple [A, B, C] , B] (get_snd)

  def apply (message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [B] ] =
    map_tile .apply (
      message
    )

}

case class UnzipTripleSndTile_ [A, B, C] () extends UnzipTripleSndTile [A, B, C]

object UnzipTripleSndTile {
  def mk [A, B, C] : UnzipTripleSndTile [A, B, C] =
    UnzipTripleSndTile_ [A, B, C] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of triples (a, b, c), and returns a sequence with the third
 * component of each triple from the input.
 */

trait UnzipTripleTrdTile [A , B , C ]
{



  def get_trd (triple : TileTriple [A, B, C] ) : C =
    triple .trd

  lazy val map_tile = MapTile .mk [TileTriple [A, B, C] , C] (get_trd)

  def apply (message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [C] ] =
    map_tile .apply (
      message
    )

}

case class UnzipTripleTrdTile_ [A, B, C] () extends UnzipTripleTrdTile [A, B, C]

object UnzipTripleTrdTile {
  def mk [A, B, C] : UnzipTripleTrdTile [A, B, C] =
    UnzipTripleTrdTile_ [A, B, C] ()
}

