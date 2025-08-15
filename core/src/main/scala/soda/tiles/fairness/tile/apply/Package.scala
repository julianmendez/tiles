package soda.tiles.fairness.tile.apply

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
import   soda.tiles.fairness.tile.map.MapTile





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

  def apply (message : TileMessage [TilePair [Boolean, Boolean] ] )
      : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      combine (message .contents .fst) (message .contents .snd)
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

  def apply (message : TileMessage [Measure] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      to_boolean (message .contents)
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

trait ProjectionPairFstTile
{



  def apply [A , B ] (message : TileMessage [TilePair [A, B] ] )
      : TileMessage [A] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      message .contents .fst)

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



  def apply [A , B ] (message : TileMessage [TilePair [A, B] ] )
      : TileMessage [B] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      message .contents .snd)

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



  def apply [A , B , C ] (message : TileMessage [TileTriple [A, B, C] ] )
      : TileMessage [A] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      message .contents .fst)

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



  def apply [A , B , C ] (message : TileMessage [TileTriple [A, B, C] ] )
      : TileMessage [B] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      message .contents .snd)

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



  def apply [A , B , C ] (message : TileMessage [TileTriple [A, B, C] ] )
      : TileMessage [C] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      message .contents .trd)

}

case class ProjectionTripleTrdTile_ () extends ProjectionTripleTrdTile

object ProjectionTripleTrdTile {
  def mk : ProjectionTripleTrdTile =
    ProjectionTripleTrdTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects two elements and returns a pair.
 */

trait TuplingPairTile
{



  def apply [A , B ] (message0 : TileMessage [A] )
      (message1 : TileMessage [B] ) : TileMessage [TilePair [A, B] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      TilePair .mk [A, B] (message0 .contents) (message1 .contents)
    )

}

case class TuplingPairTile_ () extends TuplingPairTile

object TuplingPairTile {
  def mk : TuplingPairTile =
    TuplingPairTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects three elements and returns a triple.
 */

trait TuplingTripleTile
{



  def apply [A , B , C ] (message0 : TileMessage [A] )
    (message1 : TileMessage [B] ) (message2 : TileMessage [C] )
      : TileMessage [TileTriple [A, B, C] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      TileTriple .mk [A, B, C] (message0 .contents) (message1 .contents) (message2 .contents)
    )

}

case class TuplingTripleTile_ () extends TuplingTripleTile

object TuplingTripleTile {
  def mk : TuplingTripleTile =
    TuplingTripleTile_ ()
}

