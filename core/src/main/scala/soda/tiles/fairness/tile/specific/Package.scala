package soda.tiles.fairness.tile.specific

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Pearson
import   soda.tiles.fairness.tool.PearsonMod
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tile.zip.ZipSigmaTile
import   soda.tiles.fairness.tile.fold.ReceivedSigmaPTile
import   soda.tiles.fairness.tile.zip.ZipPairTile





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

  lazy val zip_tile = ZipPairTile .mk

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
import Soda.tiles.fairness.tile.zip.ZipSigmaTile
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

