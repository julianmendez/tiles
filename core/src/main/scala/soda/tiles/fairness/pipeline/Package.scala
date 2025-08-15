package soda.tiles.fairness.pipeline

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
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
import   soda.tiles.fairness.tile.constant.AllAgentPairTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.constant.AllAgentTripleTile
import   soda.tiles.fairness.tile.fold.AllAtLeastTile
import   soda.tiles.fairness.tile.fold.AllEqual1Tile
import   soda.tiles.fairness.tile.fold.AllEqualTile
import   soda.tiles.fairness.tile.map.MapTile
import   soda.tiles.fairness.tile.specific.CorrelationTile
import   soda.tiles.fairness.tile.apply.DecisionTile
import   soda.tiles.fairness.tile.specific.FalsePosTile
import   soda.tiles.fairness.tile.map.NeededPTile
import   soda.tiles.fairness.tile.fold.PredictionPTile
import   soda.tiles.fairness.tile.apply.ProjectionPairFstTile
import   soda.tiles.fairness.tile.apply.ProjectionPairSndTile
import   soda.tiles.fairness.tile.apply.ProjectionTripleFstTile
import   soda.tiles.fairness.tile.apply.ProjectionTripleSndTile
import   soda.tiles.fairness.tile.apply.ProjectionTripleTrdTile
import   soda.tiles.fairness.tile.fold.ReceivedSigmaPTile
import   soda.tiles.fairness.tile.fold.SigmaTile
import   soda.tiles.fairness.tile.map.UnzipPairFstTile
import   soda.tiles.fairness.tile.map.UnzipPairSndTile
import   soda.tiles.fairness.tile.map.UnzipTripleFstTile
import   soda.tiles.fairness.tile.map.UnzipTripleSndTile
import   soda.tiles.fairness.tile.map.UnzipTripleTrdTile
import   soda.tiles.fairness.tile.zip.ZipPairTile
import   soda.tiles.fairness.tile.zip.ZipTripleTile





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllAgentTile
import Soda.tiles.fairness.tile.AllEqualTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
*/

/**
 * This pipeline returns 'true' when all the agents in the input receive a resource of the
 * same value, and 'false' otherwise.
 */

trait EqualityPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p_utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p_utility)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        all_agent_tile .apply (message)
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
 * This pipeline returns 'true' when all the agents in the input receive a resource that
 * satisfies their needs, and 'false' otherwise.
 */

trait EquityPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p0_need : Agent => Measure
  def   p1_utility : Resource => Measure

  lazy val at_least_tile = AllAtLeastTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p1_utility)

  lazy val needed_p_tile = NeededPTile .mk (p0_need)

  lazy val all_agent_pair_tile = AllAgentPairTile .mk

  lazy val pair_fst_tile = ProjectionPairFstTile .mk

  lazy val pair_snd_tile = ProjectionPairSndTile .mk

  def apply_on_pair (pair : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] ) : TileMessage [Boolean] =
    at_least_tile .apply (
      received_sigma_p_tile .apply (pair_fst_tile (pair) )
    ) (
      needed_p_tile .apply (pair_snd_tile (pair) )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_pair (all_agent_pair_tile .apply (message) )

}

case class EquityPipeline_ (sigma : Measure => Measure => Measure, p0_need : Agent => Measure, p1_utility : Resource => Measure) extends EquityPipeline

object EquityPipeline {
  def mk (sigma : Measure => Measure => Measure) (p0_need : Agent => Measure) (p1_utility : Resource => Measure) : EquityPipeline =
    EquityPipeline_ (sigma, p0_need, p1_utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllAgentTripleTile
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
  def   p1_result : Agent => Measure
  def   p2_with_p : Agent => Measure
  def   p3_acceptable_bias : Measure

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val all_agent_triple_tile = AllAgentTripleTile .mk

  lazy val triple_fst_tile = ProjectionTripleFstTile .mk

  lazy val triple_snd_tile = ProjectionTripleSndTile .mk

  lazy val triple_trd_tile = ProjectionTripleTrdTile .mk

  lazy val zip_pair_tile = ZipPairTile .mk

  lazy val zip_triple_tile = ZipTripleTile .mk

  lazy val prediction_p_tile = PredictionPTile .mk (p0_evaluation)

  lazy val result_p_tile = MapTile .mk [Agent, Measure] (p1_result)

  lazy val with_p_tile = MapTile .mk [Agent, Measure] (p2_with_p)

  lazy val false_pos_tile = FalsePosTile .mk

  lazy val correlation_tile = CorrelationTile .mk

  lazy val decision_tile = DecisionTile .mk (p3_acceptable_bias)

  def get_correlation_plumbing (message0 : TileMessage [Seq [Agent] ] )
    (message1 : TileMessage [Seq [Agent] ] ) (message2 : TileMessage [Seq [Agent] ] )
      : TileMessage [Measure] =
    correlation_tile .apply (
      false_pos_tile .apply (prediction_p_tile .apply (message0) ) (result_p_tile .apply (message1) )
    ) (with_p_tile .apply (message2) )

  def apply_on_triple (triple : TileMessage [TileTriple [Seq [Agent] , Seq [Agent] , Seq [Agent] ] ] )
      : TileMessage [Boolean] =
    decision_tile .apply (
      get_correlation_plumbing (
        triple_fst_tile .apply (triple) ) (
        triple_snd_tile .apply (triple) ) (
        triple_trd_tile .apply (triple) )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_triple (all_agent_triple_tile .apply (message) )

}

case class UnbiasednessPipeline_ (p0_evaluation : Resource => Measure, p1_result : Agent => Measure, p2_with_p : Agent => Measure, p3_acceptable_bias : Measure) extends UnbiasednessPipeline

object UnbiasednessPipeline {
  def mk (p0_evaluation : Resource => Measure) (p1_result : Agent => Measure) (p2_with_p : Agent => Measure) (p3_acceptable_bias : Measure) : UnbiasednessPipeline =
    UnbiasednessPipeline_ (p0_evaluation, p1_result, p2_with_p, p3_acceptable_bias)
}

