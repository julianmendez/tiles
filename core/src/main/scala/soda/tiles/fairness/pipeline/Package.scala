package soda.tiles.fairness.pipeline

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.composite.AllAtLeastTile
import   soda.tiles.fairness.tile.composite.AllEqualTile
import   soda.tiles.fairness.tile.composite.CorrelationAbsTile
import   soda.tiles.fairness.tile.composite.FalsePosTile
import   soda.tiles.fairness.tile.composite.PredictionPTile
import   soda.tiles.fairness.tile.composite.AllAgentPairTile
import   soda.tiles.fairness.tile.composite.AllAgentTripleTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.apply.DecisionTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionPairFstTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionPairSndTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionTripleFstTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionTripleSndTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionTripleTrdTile
import   soda.tiles.fairness.tile.derived.map.NeedsTile
import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tile.primitive.ZipTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.MeasureMod
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.OutcomeMod
import   soda.tiles.fairness.tool.PearsonCorrDirect
import   soda.tiles.fairness.tool.PearsonCorrCovariance
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This composite tile returns all the accumulated resources of the agents.
 */

trait AllAgentAccumulatesTile
{

  def   utility : Resource => Measure

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Measure] ] =
    accumulates_tile .apply (
      all_agent_tile .apply (
        message
      )
    )

}

case class AllAgentAccumulatesTile_ (utility : Resource => Measure) extends AllAgentAccumulatesTile

object AllAgentAccumulatesTile {
  def mk (utility : Resource => Measure) : AllAgentAccumulatesTile =
    AllAgentAccumulatesTile_ (utility)
}


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

  def   p : Agent => Boolean

  lazy val zero = MeasureMod .mk .zero

  lazy val one = MeasureMod .mk .one

  def check_protected_attribute (a : Agent) : Measure =
    if ( (p (a) )
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

case class AllAgentMapGroundTruthTile_ (p : Agent => Boolean) extends AllAgentMapGroundTruthTile

object AllAgentMapGroundTruthTile {
  def mk (p : Agent => Boolean) : AllAgentMapGroundTruthTile =
    AllAgentMapGroundTruthTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This composite tile returns all the needs of the agents.
 */

trait AllAgentNeedsTile
{

  def   q : Resource => Measure

  lazy val needs_tile = NeedsTile .mk (q)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Measure] ] =
    needs_tile .apply (
      all_agent_tile .apply (
        message
      )
    )

}

case class AllAgentNeedsTile_ (q : Resource => Measure) extends AllAgentNeedsTile

object AllAgentNeedsTile {
  def mk (q : Resource => Measure) : AllAgentNeedsTile =
    AllAgentNeedsTile_ (q)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This pipeline returns 'true' when all the agents in the input receive a resource of the
 * same value, and 'false' otherwise.
 */

trait EqualityPipeline
{

  def   utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_equal_tile .apply (
      accumulates_tile .apply (
        all_agent_tile .apply (
          message
        )
      )
    )

}

case class EqualityPipeline_ (utility : Resource => Measure) extends EqualityPipeline

object EqualityPipeline {
  def mk (utility : Resource => Measure) : EqualityPipeline =
    EqualityPipeline_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.composite.AtLeastTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
import Soda.tiles.fairness.tile.derived.map.NeedsTile
import Soda.tiles.fairness.tile.derived.map.UnzipPairFstTile
import Soda.tiles.fairness.tile.derived.map.UnzipPairSndTile
*/

/**
 * This pipeline returns 'true' when all the agents in the input receive a resource that
 * satisfies their needs, and 'false' otherwise.
 */

trait EquityPipeline
{

  def   need : Agent => Measure
  def   utility : Resource => Measure

  lazy val all_at_least_tile = AllAtLeastTile .mk

  lazy val all_agent_accumulates_tile = AllAgentAccumulatesTile .mk (utility)

  lazy val all_agent_needs_tile = AllAgentNeedsTile .mk (need)

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_at_least_tile .apply (
      all_agent_accumulates_tile .apply (
        message
      )
    ) (
      all_agent_needs_tile .apply (
        message
      )
    )

}

case class EquityPipeline_ (need : Agent => Measure, utility : Resource => Measure) extends EquityPipeline

object EquityPipeline {
  def mk (need : Agent => Measure) (utility : Resource => Measure) : EquityPipeline =
    EquityPipeline_ (need, utility)
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
  def   with_p : Agent => Boolean

  lazy val all_agent_map_ground_truth_tile = AllAgentMapGroundTruthTile .mk (with_p)

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

case class UnbiasednessPipeline_ (positive_value : Resource, result_function : Agent => Resource, with_p : Agent => Boolean) extends UnbiasednessPipeline

object UnbiasednessPipeline {
  def mk (positive_value : Resource) (result_function : Agent => Resource) (with_p : Agent => Boolean) : UnbiasednessPipeline =
    UnbiasednessPipeline_ (positive_value, result_function, with_p)
}

