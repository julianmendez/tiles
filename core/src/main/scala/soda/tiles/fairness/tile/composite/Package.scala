package soda.tiles.fairness.tile.composite

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.map.SigmaTile
import   soda.tiles.fairness.tile.derived.fold.LengthTile
import   soda.tiles.fairness.tile.derived.fold.SumCountTile
import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tile.primitive.DistinctTile
import   soda.tiles.fairness.tile.primitive.FilterTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tile.primitive.TuplingPairTile
import   soda.tiles.fairness.tile.primitive.TuplingTripleTile
import   soda.tiles.fairness.tile.primitive.ZipTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.MeasureMod
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.OutcomeMod
import   soda.tiles.fairness.tool.Pearson
import   soda.tiles.fairness.tool.PearsonMod
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





/**
 * This tile takes a sequence of agents as input and returns a sequence containing, for each
 * agent in the input sequence, a measure amounting the value of all the resources given to that
 * agent. This tile requires a utility function that informs the value of each resource.
 */

trait AccumulatesTile
{

  def   utility : Resource => Measure

  lazy val zero : Measure = MeasureMod .mk .zero

  def plus (m0 : Measure , m1 : Measure) : Measure =
    MeasureMod .mk .plus (m0) (m1)

  def get_accumulated (outcome : Outcome) (a : Agent) : Measure =
    OutcomeMod .mk
      .get_resources (outcome) (a)
      .map ( resource => utility (resource) )
      .foldLeft (zero) (plus)

  def map_tile (outcome : Outcome) : MapTile [Agent, Measure] =
    MapTile .mk [Agent, Measure] ( agent => get_accumulated (outcome) (agent) )

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    map_tile (message .outcome) .apply (
      message
    )

}

case class AccumulatesTile_ (utility : Resource => Measure) extends AccumulatesTile

object AccumulatesTile {
  def mk (utility : Resource => Measure) : AccumulatesTile =
    AccumulatesTile_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sequence of pairs containing the same agent, sorted by agent, where each
 * pair of agents occurs exactly once.
 */

trait AllAgentPairTile
{



  lazy val tupling_tile = TuplingPairTile .mk [Seq [Agent] , Seq [Agent] ]

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] =
    tupling_tile .apply (
      all_agent_tile (message)
    ) (
      all_agent_tile (message)
    )

}

case class AllAgentPairTile_ () extends AllAgentPairTile

object AllAgentPairTile {
  def mk : AllAgentPairTile =
    AllAgentPairTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sequence of triples containing the same agent, sorted by agent, where
 * each triple of agents occurs exactly once.
 */

trait AllAgentTripleTile
{



  lazy val tupling_tile = TuplingTripleTile .mk [Seq [Agent] , Seq [Agent] , Seq [Agent] ]

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [TileTriple [Seq [Agent] , Seq [Agent] , Seq [Agent] ] ] =
    tupling_tile .apply (
      all_agent_tile (message)
    ) (
      all_agent_tile (message)
    ) (
      all_agent_tile (message)
    )

}

case class AllAgentTripleTile_ () extends AllAgentTripleTile

object AllAgentTripleTile {
  def mk : AllAgentTripleTile =
    AllAgentTripleTile_ ()
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



  def compare (pair : TilePair [Measure, Measure] ) : Boolean =
    ( (Comparator .mk .compareMeasure (pair .fst) (pair .snd) ) >= 0 )

  lazy val zip_tile = ZipTile .mk [Measure, Measure]

  lazy val forall_tile = ForallTile .mk [TilePair [Measure, Measure] ] (compare)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    forall_tile .apply (
      zip_tile .apply (
        message0
      ) (
        message1
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
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This tile takes a sequence of measures and returns 'true' when they are all the same.
 */

trait AllEqualTile
{



  lazy val zero = MeasureMod .mk .zero

  lazy val one = MeasureMod .mk .one

  lazy val distinct_tile = DistinctTile .mk [Measure]

  lazy val length_tile = LengthTile .mk [Measure]

  lazy val apply_tile = ApplyTile .mk [Measure, Boolean] ( m => (m == zero) || (m == one) )

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    apply_tile .apply (
      length_tile .apply (
        distinct_tile .apply (
          message
        )
      )
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
 * This tile takes a sequence of measures and returns the average of all of them, or 0 if the sequence is empty.
 */

trait AverageTile
{



  lazy val zero = MeasureMod .mk .zero

  def apply_function_with (m0 : Measure) (m1 : Measure) : Measure =
    if ( m1 == zero
    ) zero
    else MeasureMod .mk .divide (m0) (m1)

  def apply_function (pair : TilePair [Measure, Measure] ) : Measure =
    apply_function_with (pair .fst) (pair .snd)

  lazy val apply_tile = ApplyTile .mk [TilePair [Measure, Measure] , Measure] (apply_function)

  lazy val sum_count_tile = SumCountTile .mk

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Measure] =
    apply_tile .apply (
      sum_count_tile .apply (
        message
      )
    )

}

case class AverageTile_ () extends AverageTile

object AverageTile {
  def mk : AverageTile =
    AverageTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tool.ScoringTool
*/

import soda.tiles.fairness.tool.Number

/**
 * This tile computes the Pearson correlation, and for that, takes two sequences of measures.
 * The result is a number in the interval [0, 1].
 */

trait CorrelationAbsTile
{



  def conversion (m : Measure) : Option [Number] =
    m .map ( value =>
      PearsonMod .mk .abs (value / correlation_tile .percentage_constant)
    )

  lazy val apply_tile = ApplyTile .mk [Measure, Option [Number] ] (conversion)

  lazy val correlation_tile = CorrelationTile .mk

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Option [Number] ] =
    apply_tile .apply (
      correlation_tile .apply (
          message0
      ) (
        message1
      )
    )

}

case class CorrelationAbsTile_ () extends CorrelationAbsTile

object CorrelationAbsTile {
  def mk : CorrelationAbsTile =
    CorrelationAbsTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tool.ScoringTool
*/

import soda.tiles.fairness.tool.Number

/**
 * This tile computes the Pearson correlation, and for that, takes two sequences of measures.
 * The result is a measure in the interval [-100, 100]
 */

trait CorrelationTile
{



  private lazy val _measure_zero : Measure = Some (0)

  lazy val percentage_constant : Number = 100.0

  lazy val one_plus_epsilon = 1.00001

  lazy val zip_tile = ZipTile .mk [Measure, Measure]

  def get_coefficient (xlist : Seq [Number] ) (ylist : Seq [Number] ) : Option [Number] =
    PearsonMod .mk .coefficient (Pearson .mk (xlist) (ylist) )

  def to_double (m : Measure) : Number =
    m match  {
      case Some (value) => value .toDouble
      case otherwise => Double .NaN
    }

  def to_measure (d : Number) : Measure =
    Some ( (d * percentage_constant * one_plus_epsilon) .intValue)

  def get_fst_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Number] =
    lists .map ( pair => to_double (pair .fst) )

  def get_snd_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Number] =
    lists .map ( pair => to_double (pair .snd) )

  def is_all_defined (lists : Seq [TilePair [Measure, Measure] ] ) : Boolean =
    lists .forall ( pair => (pair .fst .nonEmpty) && (pair .snd .nonEmpty) )

  def process_coefficient (maybeCoefficient : Option [Number] ) : Measure =
    maybeCoefficient match  {
      case Some (value) => to_measure (value)
      case None => None
    }

  def process_tuples (lists : Seq [TilePair [Measure, Measure] ] ) : Measure =
    if ( is_all_defined (lists)
    ) process_coefficient (get_coefficient (get_fst_list (lists) ) (get_snd_list (lists) ) )
    else None

  lazy val apply_tile = ApplyTile .mk [Seq [TilePair [Measure, Measure] ] , Measure] (process_tuples)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] ) : TileMessage [Measure] =
    apply_tile .apply (
      zip_tile .apply (
        message0
      ) (
        message1
      )
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

  def   phi : A => Boolean

  lazy val zero = MeasureMod .mk .zero

  lazy val filter_tile = FilterTile .mk [A] (phi)

  lazy val length_tile = LengthTile .mk [A]

  lazy val apply_tile = ApplyTile .mk [Measure, Boolean] ( m => ! (m == zero) )

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Boolean] =
    apply_tile .apply (
      length_tile .apply (
        filter_tile .apply (
          message
        )
      )
    )

}

case class ExistsTile_ [A] (phi : A => Boolean) extends ExistsTile [A]

object ExistsTile {
  def mk [A] (phi : A => Boolean) : ExistsTile [A] =
    ExistsTile_ [A] (phi)
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



  lazy val zero : Measure = MeasureMod .mk .zero

  lazy val one : Measure = MeasureMod .mk .one

  def sigma (m0 : Measure) (m1 : Measure) : Measure =
    if ( (m0 == one) && (m1 == zero)
    ) one
    else zero

  lazy val zip_sigma_tile = ZipSigmaTile .mk (sigma)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Seq [Measure] ] =
    zip_sigma_tile .apply (
      message0
    ) (
      message1
    )

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

  def   phi : A => Boolean

  lazy val zero = MeasureMod .mk .zero

  def not_phi (x : A) : Boolean =
    ! phi (x)

  lazy val filter_tile = FilterTile .mk [A] (not_phi)

  lazy val length_tile = LengthTile .mk [A]

  lazy val apply_tile = ApplyTile .mk [Measure, Boolean] ( m => m == zero)

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Boolean] =
    apply_tile .apply (
      length_tile .apply (
        filter_tile .apply (
          message
        )
      )
    )

}

case class ForallTile_ [A] (phi : A => Boolean) extends ForallTile [A]

object ForallTile {
  def mk [A] (phi : A => Boolean) : ForallTile [A] =
    ForallTile_ [A] (phi)
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

  lazy val zero : Measure = MeasureMod .mk .zero

  lazy val one : Measure = MeasureMod .mk .one

  def normalize (m : Measure) : Measure =
    if ( (m == zero)
    ) zero
    else one

  lazy val map_tile = MapTile .mk [Measure, Measure] (normalize)

  lazy val accumulates_tile = AccumulatesTile .mk (p)

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    map_tile .apply (
      accumulates_tile .apply (
        message
      )
    )

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
 * This tile takes two sequences of measures as input, and returns a sequence such that,
 * for each pair (m0, m1) in the input, is m = sigma (m0, m1), where sigma is a given function
 * to combine measures.
 */

trait ZipSigmaTile
{

  def   sigma : Measure => Measure => Measure

  lazy val zip_tile = ZipTile .mk [Measure, Measure]

  lazy val sigma_tile = SigmaTile .mk (sigma)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Seq [Measure] ] =
    sigma_tile .apply (
      zip_tile .apply (
        message0
      ) (
        message1
      )
    )

}

case class ZipSigmaTile_ (sigma : Measure => Measure => Measure) extends ZipSigmaTile

object ZipSigmaTile {
  def mk (sigma : Measure => Measure => Measure) : ZipSigmaTile =
    ZipSigmaTile_ (sigma)
}

