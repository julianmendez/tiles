package soda.tiles.fairness.tile.map

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
import   soda.tiles.fairness.tile.zip.ZipPairTile

/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of measures as input and applies a function to each of the
 * elements in the input, and return the result as output.
 */

trait MapTile [A, B ]
{

  def   p : A => B

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Seq [B] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( measure => p (measure) ) )
    )

}

case class MapTile_ [A, B] (p : A => B) extends MapTile [A, B]

object MapTile {
  def mk [A, B] (p : A => B) : MapTile [A, B] =
    MapTile_ [A, B] (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.map.MapTile
*/

/**
 * This tile is a particular case of an 'MapTile', where the attribute is 'needed'.
 */

trait NeededPTile
{

  def   p : Agent => Measure

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    MapTile .mk [Agent, Measure] (p) .apply (message)

}

case class NeededPTile_ (p : Agent => Measure) extends NeededPTile

object NeededPTile {
  def mk (p : Agent => Measure) : NeededPTile =
    NeededPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pair of measures as input, and returns a sequence such that,
 * for each pair (m0, m1) in the input, is m = sigma (m0, m1), where sigma is a given function
 * to combine measures.
 */

trait SigmaTile
{

  def   sigma : Measure => Measure => Measure

  lazy val zip_tile = ZipPairTile .mk

  def apply_zipped (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
      : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (message .contents)
        .map ( pair => sigma (pair .fst) (pair .snd) )
    )

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Seq [Measure] ] =
    apply_zipped (
      zip_tile .apply (message0) (message1)
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

