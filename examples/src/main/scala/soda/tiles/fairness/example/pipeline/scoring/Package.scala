package soda.tiles.fairness.example.pipeline.scoring

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.CorrelationAbsTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.MeasureMod
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.OutcomeMod
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage

/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This composite tile returns all the the false positives with
 * respect to a prediction (the outcome) and a result function.
 */

trait AllAgentMapFalsePositiveTile
{

  def   positive_value : Resource
  def   result_function : Agent => Resource

  lazy val zero = MeasureMod .mk .zero

  lazy val one = MeasureMod .mk .one

  def pred (outcome : Outcome) (a : Agent) : Boolean =
    OutcomeMod .mk
      .get_resources (outcome) (a)
      .contains (positive_value)

  def check_false_positive (outcome : Outcome) (a : Agent) : Measure =
    if ( ( (pred (outcome) (a) ) && (! (result_function (a) == positive_value) ) )
    ) one
    else zero

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Measure] ] =
    MapTile .mk [Agent, Measure] (
       agent =>
        check_false_positive (message .outcome) (agent)
    ) .apply (
      all_agent_tile .apply (
        message
      )
    )

}

case class AllAgentMapFalsePositiveTile_ (positive_value : Resource, result_function : Agent => Resource) extends AllAgentMapFalsePositiveTile

object AllAgentMapFalsePositiveTile {
  def mk (positive_value : Resource) (result_function : Agent => Resource) : AllAgentMapFalsePositiveTile =
    AllAgentMapFalsePositiveTile_ (positive_value, result_function)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This composite tile returns, for the all agents, 1 if the agent has a protected
 * attribute, and 0 otherwise.
 */

trait AllAgentMapGroundTruthTile
{

  def   protected_attribute : Agent => Boolean

  lazy val zero = MeasureMod .mk .zero

  lazy val one = MeasureMod .mk .one

  def check_protected_attribute (a : Agent) : Measure =
    if ( (protected_attribute (a) )
    ) one
    else zero

  lazy val map_tile = MapTile .mk (check_protected_attribute)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Measure] ] =
    map_tile .apply (
      all_agent_tile .apply (
        message
      )
    )

}

case class AllAgentMapGroundTruthTile_ (protected_attribute : Agent => Boolean) extends AllAgentMapGroundTruthTile

object AllAgentMapGroundTruthTile {
  def mk (protected_attribute : Agent => Boolean) : AllAgentMapGroundTruthTile =
    AllAgentMapGroundTruthTile_ (protected_attribute)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.composite.AllAgentTripleTile
import Soda.tiles.fairness.tile.CorrelationTile
import Soda.tiles.fairness.tile.DecisionTile
import Soda.tiles.fairness.tile.FalsePosTile
import Soda.tiles.fairness.tile.PredictionPTile
import Soda.tiles.fairness.tile.UnzipTripleFstTile
import Soda.tiles.fairness.tile.UnzipTripleSndTile
import Soda.tiles.fairness.tile.UnzipTripleTrdTile
import Soda.tiles.fairness.tile.WithPTile
*/

/**
 * This pipeline measures the bias in a given scenario by contrasting
 * false positives and those with a protected attribute.
 */

trait UnbiasednessPipeline
{

  def   positive_value : Resource
  def   result_function : Agent => Resource
  def   protected_attribute : Agent => Boolean

  lazy val all_agent_map_ground_truth_tile = AllAgentMapGroundTruthTile .mk (protected_attribute)

  lazy val all_agent_map_false_positive_tile = AllAgentMapFalsePositiveTile .mk (positive_value) (result_function)

  lazy val correlation_abs_tile = CorrelationAbsTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Option [Number] ] =
    correlation_abs_tile .apply (
      all_agent_map_false_positive_tile .apply (
        message
      )
    ) (
      all_agent_map_ground_truth_tile .apply (
        message
      )
    )

}

case class UnbiasednessPipeline_ (positive_value : Resource, result_function : Agent => Resource, protected_attribute : Agent => Boolean) extends UnbiasednessPipeline

object UnbiasednessPipeline {
  def mk (positive_value : Resource) (result_function : Agent => Resource) (protected_attribute : Agent => Boolean) : UnbiasednessPipeline =
    UnbiasednessPipeline_ (positive_value, result_function, protected_attribute)
}

