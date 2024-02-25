package soda.tiles.fairness.pipeline

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
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tile.AllActorPairTile
import   soda.tiles.fairness.tile.AllActorTile
import   soda.tiles.fairness.tile.AllActorTripleTile
import   soda.tiles.fairness.tile.AllAtLeastTile
import   soda.tiles.fairness.tile.AllEqual1Tile
import   soda.tiles.fairness.tile.AllEqualTile
import   soda.tiles.fairness.tile.AttributePTile
import   soda.tiles.fairness.tile.CorrelationTile
import   soda.tiles.fairness.tile.DecisionTile
import   soda.tiles.fairness.tile.FalsePosTile
import   soda.tiles.fairness.tile.NeededPTile
import   soda.tiles.fairness.tile.PredictionPTile
import   soda.tiles.fairness.tile.ReceivedSigmaPTile
import   soda.tiles.fairness.tile.ResultPTile
import   soda.tiles.fairness.tile.SigmaTile
import   soda.tiles.fairness.tile.UnzipPairFstTile
import   soda.tiles.fairness.tile.UnzipPairSndTile
import   soda.tiles.fairness.tile.UnzipTripleFstTile
import   soda.tiles.fairness.tile.UnzipTripleSndTile
import   soda.tiles.fairness.tile.UnzipTripleTrdTile
import   soda.tiles.fairness.tile.WithPTile
import   soda.tiles.fairness.tile.ZipTile





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllActorTile
import Soda.tiles.fairness.tile.AllEqualTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
*/

/**
 * This pipeline returns 'true' when all the actors in the input receive a resource of the
 * same value, and 'false' otherwise.
 */

trait EqualityPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p_utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p_utility)

  lazy val all_actor_tile = AllActorTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        all_actor_tile .apply (message)
      )
    )

}

case class EqualityPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure) extends EqualityPipeline

object EqualityPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) : EqualityPipeline =
    EqualityPipeline_ (sigma, p_utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AtLeastTile
import Soda.tiles.fairness.tile.NeededPTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
import Soda.tiles.fairness.tile.UnzipPairFstTile
import Soda.tiles.fairness.tile.UnzipPairSndTile
import Soda.tiles.fairness.tile.ZipTile
*/

/**
 * This pipeline returns 'true' when all the actors in the input receive a resource that
 * satisfies their needs, and 'false' otherwise.
 */

trait EquityPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p0_need : Actor => Measure
  def   p1_utility : Resource => Measure

  lazy val at_least_tile = AllAtLeastTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p1_utility)

  lazy val needed_p_tile = NeededPTile .mk (p0_need)

  lazy val all_actor_pair_tile = AllActorPairTile .mk

  lazy val unzip_fst_tile = UnzipPairFstTile .mk

  lazy val unzip_snd_tile = UnzipPairSndTile .mk

  lazy val zip_tile = ZipTile .mk

  def get_branch_0 (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    received_sigma_p_tile .apply (unzip_fst_tile .apply (message) )

  def get_branch_1 (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    needed_p_tile .apply (unzip_snd_tile .apply (message) )

  def zip_branches (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Seq [TilePair [Measure, Measure] ] ] =
    zip_tile .apply (get_branch_0 (message) ) (get_branch_1 (message) )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    at_least_tile .apply (
      zip_branches (
        all_actor_pair_tile .apply (message)
      )
    )

}

case class EquityPipeline_ (sigma : Measure => Measure => Measure, p0_need : Actor => Measure, p1_utility : Resource => Measure) extends EquityPipeline

object EquityPipeline {
  def mk (sigma : Measure => Measure => Measure) (p0_need : Actor => Measure) (p1_utility : Resource => Measure) : EquityPipeline =
    EquityPipeline_ (sigma, p0_need, p1_utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllActorTripleTile
import Soda.tiles.fairness.tile.CorrelationTile
import Soda.tiles.fairness.tile.DecisionTile
import Soda.tiles.fairness.tile.FalsePosTile
import Soda.tiles.fairness.tile.PredictionPTile
import Soda.tiles.fairness.tile.ResultPTile
import Soda.tiles.fairness.tile.UnzipTripleFstTile
import Soda.tiles.fairness.tile.UnzipTripleSndTile
import Soda.tiles.fairness.tile.UnzipTripleTrdTile
import Soda.tiles.fairness.tile.WithPTile
import Soda.tiles.fairness.tile.ZipTile
*/

/**
 * This pipeline checks unbiasedness for a given scenario.
 */

trait UnbiasednessPipeline
{

  def   p0_evaluation : Resource => Measure
  def   p1_result : Actor => Measure
  def   p2_with_p : Actor => Measure
  def   p3_acceptable_bias : Measure

  lazy val all_actor_triple_tile = AllActorTripleTile .mk

  lazy val unzip_fst_tile = UnzipTripleFstTile .mk

  lazy val unzip_snd_tile = UnzipTripleSndTile .mk

  lazy val unzip_trd_tile = UnzipTripleTrdTile .mk

  lazy val zip_tile = ZipTile .mk

  lazy val prediction_p_tile = PredictionPTile .mk (p0_evaluation)

  lazy val result_p_tile = ResultPTile .mk (p1_result)

  lazy val with_p_tile = WithPTile .mk (p2_with_p)

  lazy val false_pos_tile = FalsePosTile .mk

  lazy val correlation_tile = CorrelationTile .mk

  lazy val decision_tile = DecisionTile .mk (p3_acceptable_bias)

  def get_prediction (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    prediction_p_tile .apply (unzip_fst_tile .apply (message) )

  def get_result (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    result_p_tile .apply (unzip_snd_tile .apply (message) )

  def get_with_p (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    with_p_tile .apply (unzip_trd_tile .apply (message) )

  def get_false_pos (prediction : TileMessage [Seq [Measure] ] ) (
      result : TileMessage [Seq [Measure] ] ) : TileMessage [Seq [Measure] ] =
    false_pos_tile .apply (zip_tile .apply (prediction) (result) )

  def get_correlation (false_pos : TileMessage [Seq [Measure] ] ) (
      with_p : TileMessage [Seq [Measure] ] ) : TileMessage [Measure] =
    correlation_tile .apply (zip_tile .apply (false_pos) (with_p) )

  def get_correlation_plumbing (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Measure] =
    get_correlation (
      get_false_pos (get_prediction (message) ) (get_result (message) )
    ) (get_with_p (message) )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    decision_tile .apply (
      get_correlation_plumbing (
        all_actor_triple_tile .apply (message)
      )
    )

}

case class UnbiasednessPipeline_ (p0_evaluation : Resource => Measure, p1_result : Actor => Measure, p2_with_p : Actor => Measure, p3_acceptable_bias : Measure) extends UnbiasednessPipeline

object UnbiasednessPipeline {
  def mk (p0_evaluation : Resource => Measure) (p1_result : Actor => Measure) (p2_with_p : Actor => Measure) (p3_acceptable_bias : Measure) : UnbiasednessPipeline =
    UnbiasednessPipeline_ (p0_evaluation, p1_result, p2_with_p, p3_acceptable_bias)
}

