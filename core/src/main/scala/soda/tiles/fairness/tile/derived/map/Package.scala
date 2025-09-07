package soda.tiles.fairness.tile.derived.map

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple

/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import soda.tiles.fairness.tile.primitive.MapTile
*/

/**
 * This tile is a particular case of an 'MapTile', where the attribute is 'needed'.
 */

trait NeedsTile
{

  def   q : Agent => Measure

  lazy val map_file = MapTile .mk [Agent, Measure] (q)

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    map_file
      .apply (message)

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
 * This tile takes a sequence of pairs (a, b), and returns a sequence with the first
 * component of each pair from the input.
 */

trait UnzipPairFstTile
{



  def get_fst [A , B ] (pair : TilePair [A, B] ) : A =
    pair .fst

  def apply [A , B ] (message : TileMessage [Seq [TilePair [A, B] ] ] )
      : TileMessage [Seq [A] ] =
    MapTile .mk [TilePair [A, B] , A] (get_fst)
      .apply (message)

}

case class UnzipPairFstTile_ () extends UnzipPairFstTile

object UnzipPairFstTile {
  def mk : UnzipPairFstTile =
    UnzipPairFstTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pairs (a, b), and returns a sequence with the second
 * component of each pair from the input.
 */

trait UnzipPairSndTile
{



  def get_snd [A , B ] (pair : TilePair [A, B] ) : B =
    pair .snd

  def apply [A , B ] (message : TileMessage [Seq [TilePair [A, B] ] ] )
      : TileMessage [Seq [B] ] =
    MapTile .mk [TilePair [A, B] , B] (get_snd)
      .apply (message)

}

case class UnzipPairSndTile_ () extends UnzipPairSndTile

object UnzipPairSndTile {
  def mk : UnzipPairSndTile =
    UnzipPairSndTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of triples (a, b, c), and returns a sequence with the first
 * component of each triple from the input.
 */

trait UnzipTripleFstTile
{



  def get_fst [A , B , C ] (triple : TileTriple [A, B, C] ) : A =
    triple .fst

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [A] ] =
    MapTile .mk [TileTriple [A, B, C] , A] (get_fst)
      .apply (message)

}

case class UnzipTripleFstTile_ () extends UnzipTripleFstTile

object UnzipTripleFstTile {
  def mk : UnzipTripleFstTile =
    UnzipTripleFstTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of triples (a, b, c), and returns a sequence with the second
 * component of each triple from the input.
 */

trait UnzipTripleSndTile
{



  def get_snd [A , B , C ] (triple : TileTriple [A, B, C] ) : B =
    triple .snd

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [B] ] =
    MapTile .mk [TileTriple [A, B, C] , B] (get_snd)
      .apply (message)

}

case class UnzipTripleSndTile_ () extends UnzipTripleSndTile

object UnzipTripleSndTile {
  def mk : UnzipTripleSndTile =
    UnzipTripleSndTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of triples (a, b, c), and returns a sequence with the third
 * component of each triple from the input.
 */

trait UnzipTripleTrdTile
{



  def get_trd [A , B , C ] (triple : TileTriple [A, B, C] ) : C =
    triple .trd

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [C] ] =
    MapTile .mk [TileTriple [A, B, C] , C] (get_trd)
      .apply (message)

}

case class UnzipTripleTrdTile_ () extends UnzipTripleTrdTile

object UnzipTripleTrdTile {
  def mk : UnzipTripleTrdTile =
    UnzipTripleTrdTile_ ()
}

