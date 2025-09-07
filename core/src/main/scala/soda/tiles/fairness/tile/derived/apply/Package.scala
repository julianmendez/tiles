package soda.tiles.fairness.tile.derived.apply

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a pair of boolean as input and returns a boolean according to the provided
 * function.
 */

trait CombineBooleanTile
{

  def   combine : Boolean => Boolean => Boolean

  def combine_pair (pair : TilePair [Boolean, Boolean] ) : Boolean =
    combine (pair .fst) (pair .snd)

  lazy val apply_tile = ApplyTile .mk [TilePair [Boolean, Boolean] , Boolean] (combine_pair)

  def apply (message : TileMessage [TilePair [Boolean, Boolean] ] ) : TileMessage [Boolean] =
    apply_tile .apply (message)

}

case class CombineBooleanTile_ (combine : Boolean => Boolean => Boolean) extends CombineBooleanTile

object CombineBooleanTile {
  def mk (combine : Boolean => Boolean => Boolean) : CombineBooleanTile =
    CombineBooleanTile_ (combine)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile works as a binary threshold for a measure. It returns 'true' if the input measure
 * is than or equal to a given threshold, and 'false' otherwise.
 */

trait DecisionTile
{

  def   maximum_acceptable_bias_percentage : Measure

  def to_boolean (m : Measure) : Boolean =
    ( Comparator .mk
       .compareMeasure (m) (maximum_acceptable_bias_percentage) ) <= 0

  lazy val apply_tile = ApplyTile .mk [Measure, Boolean] (to_boolean)

  def apply (message : TileMessage [Measure] ) : TileMessage [Boolean] =
    apply_tile .apply (message)

}

case class DecisionTile_ (maximum_acceptable_bias_percentage : Measure) extends DecisionTile

object DecisionTile {
  def mk (maximum_acceptable_bias_percentage : Measure) : DecisionTile =
    DecisionTile_ (maximum_acceptable_bias_percentage)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the first element of a pair.
 */

trait ProjectionPairFstTile
{



  def get_fst [A , B ] (pair : TilePair [A, B] ) : A =
    pair .fst

  def apply [A , B ] (message : TileMessage [TilePair [A, B] ] ) : TileMessage [A] =
    ApplyTile .mk [TilePair [A, B] , A] (get_fst)
      .apply (message)

}

case class ProjectionPairFstTile_ () extends ProjectionPairFstTile

object ProjectionPairFstTile {
  def mk : ProjectionPairFstTile =
    ProjectionPairFstTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the second element of a pair.
 */

trait ProjectionPairSndTile
{



  def get_snd [A , B ] (pair : TilePair [A, B] ) : B =
    pair .snd

  def apply [A , B ] (message : TileMessage [TilePair [A, B] ] ) : TileMessage [B] =
    ApplyTile .mk [TilePair [A, B] , B] (get_snd)
      .apply (message)

}

case class ProjectionPairSndTile_ () extends ProjectionPairSndTile

object ProjectionPairSndTile {
  def mk : ProjectionPairSndTile =
    ProjectionPairSndTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the first element of a triple.
 */

trait ProjectionTripleFstTile
{



  def get_fst [A , B , C ] (triple : TileTriple [A, B, C] ) : A =
    triple .fst

  def apply [A , B , C ] (message : TileMessage [TileTriple [A, B, C] ] )
      : TileMessage [A] =
    ApplyTile .mk [TileTriple [A, B, C] , A] (get_fst)
      .apply (message)

}

case class ProjectionTripleFstTile_ () extends ProjectionTripleFstTile

object ProjectionTripleFstTile {
  def mk : ProjectionTripleFstTile =
    ProjectionTripleFstTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the second element of a triple.
 */

trait ProjectionTripleSndTile
{



  def get_snd [A , B , C ] (triple : TileTriple [A, B, C] ) : B =
    triple .snd

  def apply [A , B , C ] (message : TileMessage [TileTriple [A, B, C] ] )
      : TileMessage [B] =
    ApplyTile .mk [TileTriple [A, B, C] , B] (get_snd)
      .apply (message)

}

case class ProjectionTripleSndTile_ () extends ProjectionTripleSndTile

object ProjectionTripleSndTile {
  def mk : ProjectionTripleSndTile =
    ProjectionTripleSndTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the third element of a triple.
 */

trait ProjectionTripleTrdTile
{



  def get_trd [A , B , C ] (triple : TileTriple [A, B, C] ) : C =
    triple .trd

  def apply [A , B , C ] (message : TileMessage [TileTriple [A, B, C] ] )
      : TileMessage [C] =
    ApplyTile .mk [TileTriple [A, B, C] , C] (get_trd)
      .apply (message)

}

case class ProjectionTripleTrdTile_ () extends ProjectionTripleTrdTile

object ProjectionTripleTrdTile {
  def mk : ProjectionTripleTrdTile =
    ProjectionTripleTrdTile_ ()
}

