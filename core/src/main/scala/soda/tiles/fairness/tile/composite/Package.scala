package soda.tiles.fairness.tile.composite

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.derived.map.SigmaTile
import   soda.tiles.fairness.tile.primitive.ZipTile
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



  def compare (pair : TilePair [Measure, Measure] ) : Boolean =
    ( (Comparator .mk .compareMeasure (pair .fst) (pair .snd) ) >= 0 )

  lazy val zip_tile = ZipTile .mk

  lazy val forall_tile = ForallTile .mk [TilePair [Measure, Measure] ] (compare)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    forall_tile (
      zip_tile .apply [Measure, Measure] (message0) (message1)
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
import Soda.tiles.fairness.tool.ScoringTool
*/

import soda.tiles.fairness.tool.Number

/**
 * This tile computes the Pearson correlation, and for that, takes two sequences of measures,
 * and computes a single measure.
 */

trait CorrelationTile
{



  private lazy val _measure_zero : Measure = Some (0)

  private lazy val _percentage_constant : Number = 100.0

  lazy val zip_tile = ZipTile .mk

  def get_coefficient (xlist : Seq [Number] ) (ylist : Seq [Number] ) : Number =
    PearsonMod .mk .coefficient (Pearson .mk (xlist) (ylist) )

  def to_double (m : Measure) : Number =
    if ( (m == _measure_zero)
    ) 0.0
    else 1.0

  def to_measure (d : Number) : Measure =
    Some ( (d * _percentage_constant) .intValue)

  def get_fst_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Number] =
    lists .map ( pair => to_double (pair .fst) )

  def get_snd_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Number] =
    lists .map ( pair => to_double (pair .snd) )

  def process_tuples (lists : Seq [TilePair [Measure, Measure] ] ) : Measure =
    to_measure (get_coefficient (get_fst_list (lists) ) (get_snd_list (lists) ) )

  def apply_zipped (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Measure] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      process_tuples (message .contents)
    )

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] ) : TileMessage [Measure] =
    apply_zipped (
      zip_tile .apply [Measure, Measure] (message0) (message1)
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
 * This tile takes a sequence of elements and returns 'true' when at least one element in the input
 * satisfies a property.
 */

trait ExistsTile [A]
{

  def   p : A => Boolean

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .exists ( elem => p (elem) ) )
    )

}

case class ExistsTile_ [A] (p : A => Boolean) extends ExistsTile [A]

object ExistsTile {
  def mk [A] (p : A => Boolean) : ExistsTile [A] =
    ExistsTile_ [A] (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import soda.tiles.fairness.tile.composite.ZipSigmaTile
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

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Seq [Measure] ] =
    ZipSigmaTile .mk (sigma) .apply (message0) (message1)

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
 * This tile takes a sequence of elements and returns 'true' when all the elements in the input
 * satisfy a property.
 */

trait ForallTile [A]
{

  def   p : A => Boolean

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .forall ( elem => p (elem) ) )
    )

}

case class ForallTile_ [A] (p : A => Boolean) extends ForallTile [A]

object ForallTile {
  def mk [A] (p : A => Boolean) : ForallTile [A] =
    ForallTile_ [A] (p)
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

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
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
 * This tile takes a sequence of agents as input and returns a sequence containing, for each
 * agent in the input sequence, a measure amounting the value of all the resources given to that
 * agent. This tile requires a function to count multiple resources, and another function that
 * informs the value of each resource.
 */

trait ReceivedSigmaPTile
{

  def   sigma : Measure => Measure => Measure
  def   p : Resource => Measure

  private def _sigma2 (m0 : Measure , m1 : Measure) : Measure =
    sigma (m0) (m1)

  private lazy val _measure_zero : Measure = Some (0)

  def get_assignment (assignments : Seq [Assignment] ) (a : Agent) : Option [Assignment] =
    assignments . find ( assignment => (assignment .agent) == a)

  def get_measure (outcome : Outcome) (a : Agent) : Measure =
    (get_assignment (outcome .assignments) (a) )
      .map ( assignment => p (assignment .resource) )
      .foldLeft (_measure_zero) (_sigma2)

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( agent => get_measure (message .outcome) (agent) ) )
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
*/

/**
 * This tile takes two sequences of measures as input, and returns a sequence such that,
 * for each pair (m0, m1) in the input, is m = sigma (m0, m1), where sigma is a given function
 * to combine measures.
 */

trait ZipSigmaTile
{

  def   sigma : Measure => Measure => Measure

  lazy val zip_tile = ZipTile .mk

  lazy val sigma_tile = SigmaTile .mk (sigma)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Seq [Measure] ] =
    sigma_tile .apply (
      zip_tile .apply (message0) (message1)
    )

}

case class ZipSigmaTile_ (sigma : Measure => Measure => Measure) extends ZipSigmaTile

object ZipSigmaTile {
  def mk (sigma : Measure => Measure => Measure) : ZipSigmaTile =
    ZipSigmaTile_ (sigma)
}

