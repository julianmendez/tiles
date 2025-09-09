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
    apply_tile .apply (
      message
    )

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
    apply_tile .apply (
      message
    )

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

trait ProjectionPairFstTile [A , B ]
{



  def get_fst (pair : TilePair [A, B] ) : A =
    pair .fst

  lazy val apply_tile = ApplyTile .mk [TilePair [A, B] , A] (get_fst)

  def apply (message : TileMessage [TilePair [A, B] ] ) : TileMessage [A] =
    apply_tile .apply (
      message
    )

}

case class ProjectionPairFstTile_ [A, B] () extends ProjectionPairFstTile [A, B]

object ProjectionPairFstTile {
  def mk [A, B] : ProjectionPairFstTile [A, B] =
    ProjectionPairFstTile_ [A, B] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the second element of a pair.
 */

trait ProjectionPairSndTile [A , B ]
{



  def get_snd (pair : TilePair [A, B] ) : B =
    pair .snd

  lazy val apply_tile = ApplyTile .mk [TilePair [A, B] , B] (get_snd)

  def apply (message : TileMessage [TilePair [A, B] ] ) : TileMessage [B] =
    apply_tile .apply (
      message
    )

}

case class ProjectionPairSndTile_ [A, B] () extends ProjectionPairSndTile [A, B]

object ProjectionPairSndTile {
  def mk [A, B] : ProjectionPairSndTile [A, B] =
    ProjectionPairSndTile_ [A, B] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the first element of a triple.
 */

trait ProjectionTripleFstTile [A , B , C ]
{



  def get_fst (triple : TileTriple [A, B, C] ) : A =
    triple .fst

  lazy val apply_tile = ApplyTile .mk [TileTriple [A, B, C] , A] (get_fst)

  def apply (message : TileMessage [TileTriple [A, B, C] ] ) : TileMessage [A] =
    apply_tile .apply (
      message
    )

}

case class ProjectionTripleFstTile_ [A, B, C] () extends ProjectionTripleFstTile [A, B, C]

object ProjectionTripleFstTile {
  def mk [A, B, C] : ProjectionTripleFstTile [A, B, C] =
    ProjectionTripleFstTile_ [A, B, C] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the second element of a triple.
 */

trait ProjectionTripleSndTile [A , B , C ]
{



  def get_snd (triple : TileTriple [A, B, C] ) : B =
    triple .snd

  lazy val apply_tile = ApplyTile .mk [TileTriple [A, B, C] , B] (get_snd)

  def apply (message : TileMessage [TileTriple [A, B, C] ] ) : TileMessage [B] =
    apply_tile .apply (
      message
    )

}

case class ProjectionTripleSndTile_ [A, B, C] () extends ProjectionTripleSndTile [A, B, C]

object ProjectionTripleSndTile {
  def mk [A, B, C] : ProjectionTripleSndTile [A, B, C] =
    ProjectionTripleSndTile_ [A, B, C] ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns the third element of a triple.
 */

trait ProjectionTripleTrdTile [A , B , C ]
{



  def get_trd (triple : TileTriple [A, B, C] ) : C =
    triple .trd

  lazy val apply_tile = ApplyTile .mk [TileTriple [A, B, C] , C] (get_trd)

  def apply (message : TileMessage [TileTriple [A, B, C] ] ) : TileMessage [C] =
    apply_tile .apply (
      message
    )

}

case class ProjectionTripleTrdTile_ [A, B, C] () extends ProjectionTripleTrdTile [A, B, C]

object ProjectionTripleTrdTile {
  def mk [A, B, C] : ProjectionTripleTrdTile [A, B, C] =
    ProjectionTripleTrdTile_ [A, B, C] ()
}

