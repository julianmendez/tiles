package soda.tiles.fairness.tile

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Actor
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Pearson
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TilePair_
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tool.TileTriple_





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sequence of pairs containing the same actor, sorted by actor, where each
 * pair of actors occurs exactly once.
 */

trait AllActorPairTile
{



  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [TilePair [Actor, Actor] ] ] =
    TileMessageBuilder .mk .build (
      message .context) (message .outcome) ( ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted
        .map ( actor => TilePair .mk (actor) (actor) )
    )

}

case class AllActorPairTile_ () extends AllActorPairTile

object AllActorPairTile {
  def mk : AllActorPairTile =
    AllActorPairTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sorted sequence of actors, where each actor occurs exactly once.
 */

trait AllActorTile
{



  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Actor] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted
    )

}

case class AllActorTile_ () extends AllActorTile

object AllActorTile {
  def mk : AllActorTile =
    AllActorTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sequence of triples containing the same actor, sorted by actor, where
 * each triple of actors occurs exactly once.
 */

trait AllActorTripleTile
{



  def apply (message : TileMessage [Boolean] )
      : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted
        .map ( actor => TileTriple .mk (actor) (actor) (actor) )
    )

}

case class AllActorTripleTile_ () extends AllActorTripleTile

object AllActorTripleTile {
  def mk : AllActorTripleTile =
    AllActorTripleTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pairs of measures, and compares both components (m0, m1).
 * If for each pair (m0, m1), it holds that m0 >= m1, it returns 'true, otherwise, it returns
 * 'false'.
 */

trait AllAtLeastTile
{



  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( pair =>
          (Comparator .mk
            .compareMeasure (pair .fst) (pair .snd) ) >= 0 )
        .forall ( e => e)
      )
    )

}

case class AllAtLeastTile_ () extends AllAtLeastTile

object AllAtLeastTile {
  def mk : AllAtLeastTile =
    AllAtLeastTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of measures and returns 'true' when all measures are the same as
 * the first one.
 */

trait AllEqual1Tile
{



  def apply (message : TileMessage [Seq [Measure] ] ) (list : Seq [Measure] ) : Boolean =
    list match  {
      case Nil => true
      case (x :: xs) => xs .forall ( e => x == e)
    }

}

case class AllEqual1Tile_ () extends AllEqual1Tile

object AllEqual1Tile {
  def mk : AllEqual1Tile =
    AllEqual1Tile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This tile takes a sequence of measures and returns 'true' when they are all the same.
 */

trait AllEqualTile
{



  lazy val all_equal_1_tile = AllEqual1Tile .mk

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      all_equal_1_tile .apply (message) (message .contents)
    )

}

case class AllEqualTile_ () extends AllEqualTile

object AllEqualTile {
  def mk : AllEqualTile =
    AllEqualTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of measures and returns 'true' when all the elements in the input
 * satisfy a property.
 */

trait AllSatisfyPTile
{

  def   p : Measure => Boolean

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .forall ( actor => p (actor) ) )
    )

}

case class AllSatisfyPTile_ (p : Measure => Boolean) extends AllSatisfyPTile

object AllSatisfyPTile {
  def mk (p : Measure => Boolean) : AllSatisfyPTile =
    AllSatisfyPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects two elements and returns a pair.
 */

trait AtomicZipTile
{



  def apply [A , B ] (message0 : TileMessage [A] )
      (message1 : TileMessage [B] ) : TileMessage [TilePair [A, B] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      TilePair .mk (message0 .contents) (message1 .contents)
    )

}

case class AtomicZipTile_ () extends AtomicZipTile

object AtomicZipTile {
  def mk : AtomicZipTile =
    AtomicZipTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of actors as input and returns the sequence of measures, such
 * that, each position in the output sequence is the application of a function on the
 * corresponding actor in the input.
 */

trait AttributePTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( actor => p (actor) ) )
    )

}

case class AttributePTile_ (p : Actor => Measure) extends AttributePTile

object AttributePTile {
  def mk (p : Actor => Measure) : AttributePTile =
    AttributePTile_ (p)
}


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
 * This tile computes the Pearson correlation, and for that, takes two sequences of measures,
 * and computes a single measure.
 */

trait CorrelationTile
{



  private lazy val _measure_zero : Measure = Some (0)

  private lazy val _percentage_constant : Float = 100.0

  def get_coefficient (xlist : Seq [Float] ) (ylist : Seq [Float] ) : Float =
    (Pearson .mk (xlist) (ylist) ) .coefficient

  def to_double (m : Measure) : Float =
    if ( (m == _measure_zero)
    ) 0.0
    else 1.0

  def to_measure (d : Float) : Measure =
    Some ( (d * _percentage_constant) .intValue)

  def get_fst_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Float] =
    lists .map ( pair => to_double (pair .fst) )

  def get_snd_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Float] =
    lists .map ( pair => to_double (pair .snd) )

  def process_tuples (lists : Seq [TilePair [Measure, Measure] ] ) : Measure =
    to_measure (get_coefficient (get_fst_list (lists) ) (get_snd_list (lists) ) )

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Measure] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      process_tuples (message .contents)
    )

}

case class CorrelationTile_ () extends CorrelationTile

object CorrelationTile {
  def mk : CorrelationTile =
    CorrelationTile_ ()
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
import Soda.tiles.fairness.tile.SigmaTile
*/

/**
 * This tiles takes a sequence of pairs of measures (m0, m1), and returns a sequence, which
 * for each pair is 1, if m0 = 1 and m1 = 0, and 0 otherwise.
 */

trait FalsePosTile
{



  private lazy val _measure_zero : Measure = Some (0)

  private lazy val _measure_one : Measure = Some (1)

  def sigma (m0 : Measure) (m1 : Measure) : Measure =
    if ( (m0 == _measure_one) && (m1 == _measure_zero)
    ) _measure_one
    else _measure_zero

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
      : TileMessage [Seq [Measure] ] =
    SigmaTile .mk (sigma) .apply (message)

}

case class FalsePosTile_ () extends FalsePosTile

object FalsePosTile {
  def mk : FalsePosTile =
    FalsePosTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a possibly empty sequence of actors that satisfy a given property.
 */

trait FilterActorTile
{

  def   p : Actor => Boolean

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Actor] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .filter ( actor => p (actor) ) )
    )

}

case class FilterActorTile_ (p : Actor => Boolean) extends FilterActorTile

object FilterActorTile {
  def mk (p : Actor => Boolean) : FilterActorTile =
    FilterActorTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a possibly empty sequence of measures that satisfy a given property.
 */

trait FilterMeasureTile
{

  def   p : Measure => Boolean

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .filter ( measure => p (measure) ) )
    )

}

case class FilterMeasureTile_ (p : Measure => Boolean) extends FilterMeasureTile

object FilterMeasureTile {
  def mk (p : Measure => Boolean) : FilterMeasureTile =
    FilterMeasureTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of measures as input and applies a function to each of the
 * elements in the input, and return the result as output.
 */

trait MapPTile
{

  def   p : Measure => Measure

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( measure => p (measure) ) )
    )

}

case class MapPTile_ (p : Measure => Measure) extends MapPTile

object MapPTile {
  def mk (p : Measure => Measure) : MapPTile =
    MapPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AttributePTile
*/

/**
 * This tile is a particular case of an 'AttributePTile', where the attribute is 'needed'.
 */

trait NeededPTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    AttributePTile .mk (p) .apply (message)

}

case class NeededPTile_ (p : Actor => Measure) extends NeededPTile

object NeededPTile {
  def mk (p : Actor => Measure) : NeededPTile =
    NeededPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
*/

/**
 * This tile is a particular case of a 'ReceivedSigmaPTile' where the resource is a prediction
 * score.
 */

trait PredictionPTile
{

  def   p : Resource => Measure

  private lazy val _measure_zero : Measure = Some (0)

  def measure_or (m0 : Measure) (m1 : Measure) : Measure =
    if ( (m0 == _measure_zero)
    ) m1
    else m0

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    (ReceivedSigmaPTile .mk (measure_or) (p) ) .apply (message)

}

case class PredictionPTile_ (p : Resource => Measure) extends PredictionPTile

object PredictionPTile {
  def mk (p : Resource => Measure) : PredictionPTile =
    PredictionPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of actors as input and returns a sequence containing, for each
 * actor in the input sequence, a measure amounting the value of all the resources given to that
 * actor. This tile requires a function to count multiple resources, and another function that
 * informs the value of each resource.
 */

trait ReceivedSigmaPTile
{

  def   sigma : Measure => Measure => Measure
  def   p : Resource => Measure

  private def _sigma2 (m0 : Measure , m1 : Measure) : Measure =
    sigma (m0) (m1)

  private lazy val _measure_zero : Measure = Some (0)

  def get_assignment (assignments : Seq [Assignment] ) (a : Actor) : Option [Assignment] =
    assignments . find ( assignment => (assignment .actor) == a)

  def get_measure (outcome : Outcome) (a : Actor) : Measure =
    (get_assignment (outcome .assignments) (a) )
      .map ( assignment => p (assignment .resource) )
      .foldLeft (_measure_zero) (_sigma2)

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( actor => get_measure (message .outcome) (actor) ) )
    )

}

case class ReceivedSigmaPTile_ (sigma : Measure => Measure => Measure, p : Resource => Measure) extends ReceivedSigmaPTile

object ReceivedSigmaPTile {
  def mk (sigma : Measure => Measure => Measure) (p : Resource => Measure) : ReceivedSigmaPTile =
    ReceivedSigmaPTile_ (sigma, p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AttributePTile
*/

/**
 * This tile is a particular case of an 'AttributePTile', where the attribute is the 'result'.
 * This tile is used to contrast obtained results to predicted results.
 */

trait ResultPTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    AttributePTile .mk (p) .apply (message)

}

case class ResultPTile_ (p : Actor => Measure) extends ResultPTile

object ResultPTile {
  def mk (p : Actor => Measure) : ResultPTile =
    ResultPTile_ (p)
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

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (message .contents)
        .map ( pair => sigma (pair .fst) (pair .snd) )
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



  def unzip_fst_list [A , B ] (list : Seq [TilePair [A, B] ] ) : Seq [A] =
    list .map ( pair => pair .fst)

  def apply [A , B ] (message : TileMessage [Seq [TilePair [A, B] ] ] )
      : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_fst_list (message .contents)
    )

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



  def unzip_snd_list [A , B ] (list : Seq [TilePair [A, B] ] ) : Seq [B] =
    list .map ( pair => pair .snd)

  def apply [A , B ] (message : TileMessage [Seq [TilePair [A, B] ] ] )
      : TileMessage [Seq [B] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_snd_list (message .contents)
    )

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
 * component of each pair from the input.
 */

trait UnzipTripleFstTile
{



  def unzip_fst_list [A , B , C ] (
      list : Seq [TileTriple [A, B, C] ] ) : Seq [A] =
    list .map ( triple => triple .fst)

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_fst_list (message .contents)
    )

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
 * component of each pair from the input.
 */

trait UnzipTripleSndTile
{



  def unzip_snd_list [A , B , C ] (
      list : Seq [TileTriple [A, B, C] ] ) : Seq [B] =
    list .map ( triple => triple .snd)

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [B] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_snd_list (message .contents)
    )

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
 * component of each pair from the input.
 */

trait UnzipTripleTrdTile
{



  def unzip_trd_list [A , B , C ] (
      list : Seq [TileTriple [A, B, C] ] ) : Seq [C] =
    list .map ( triple => triple .trd)

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [C] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_trd_list (message .contents)
    )

}

case class UnzipTripleTrdTile_ () extends UnzipTripleTrdTile

object UnzipTripleTrdTile {
  def mk : UnzipTripleTrdTile =
    UnzipTripleTrdTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AttributePTile
*/

/**
 * This tile is a particular case of a 'AttributePTile', that projects given property.
 */

trait WithPTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    AttributePTile .mk (p) .apply (message)

}

case class WithPTile_ (p : Actor => Measure) extends WithPTile

object WithPTile {
  def mk (p : Actor => Measure) : WithPTile =
    WithPTile_ (p)
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
      .map ( pair => TilePair_ [A, B] (pair ._1 , pair._2) )

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

