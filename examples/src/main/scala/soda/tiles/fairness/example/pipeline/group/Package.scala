package soda.tiles.fairness.example.pipeline.group

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.composite.AllAtLeastTile
import   soda.tiles.fairness.tile.composite.AllEqualTile
import   soda.tiles.fairness.tile.composite.CorrelationAbsTile
import   soda.tiles.fairness.tile.composite.ExistsTile
import   soda.tiles.fairness.tile.composite.FalsePosTile
import   soda.tiles.fairness.tile.composite.PredictionPTile
import   soda.tiles.fairness.tile.composite.AllAgentPairTile
import   soda.tiles.fairness.tile.composite.AllAgentTripleTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.constant.AllResourceTile
import   soda.tiles.fairness.tile.derived.apply.DecisionTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionPairFstTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionPairSndTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionTripleFstTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionTripleSndTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionTripleTrdTile
import   soda.tiles.fairness.tile.derived.fold.LengthTile
import   soda.tiles.fairness.tile.derived.map.NeedsTile
import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tile.primitive.CrossTile
import   soda.tiles.fairness.tile.primitive.FilterTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tile.primitive.TuplingPairTile
import   soda.tiles.fairness.tile.primitive.ZipTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.MathTool
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
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the group fairness pipeline.
 */

trait AllAgentFilterNotPLengthTile
{

  def   p : Agent => Boolean

  def not_p (a : Agent) : Boolean =
    ! (p (a) )

  lazy val other_tile = AllAgentFilterPLengthTile .mk (not_p)

  def apply (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    other_tile .apply (
      message
    )

}

case class AllAgentFilterNotPLengthTile_ (p : Agent => Boolean) extends AllAgentFilterNotPLengthTile

object AllAgentFilterNotPLengthTile {
  def mk (p : Agent => Boolean) : AllAgentFilterNotPLengthTile =
    AllAgentFilterNotPLengthTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the group fairness pipeline.
 */

trait AllAgentFilterPLengthTile
{

  def   p : Agent => Boolean

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val cross_tile = CrossTile .mk [Agent, Resource]

  lazy val length_tile = LengthTile .mk [Agent]

  def apply (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    length_tile .apply (
      FilterTile .mk [Agent] ( agent =>
        p (agent)
      ) .apply (
        all_agent_tile .apply (
          message
        )
      )
    )

}

case class AllAgentFilterPLengthTile_ (p : Agent => Boolean) extends AllAgentFilterPLengthTile

object AllAgentFilterPLengthTile {
  def mk (p : Agent => Boolean) : AllAgentFilterPLengthTile =
    AllAgentFilterPLengthTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the group fairness pipeline.
 */

trait CrossFilterNotPLengthTile
{

  def   p : Agent => Boolean

  def not_p (a : Agent) : Boolean =
    ! (p (a) )

  lazy val other_tile = CrossFilterPLengthTile .mk (not_p)

  def apply (message0 : TileMessage [Seq [Agent] ] )  (message1 : TileMessage [Seq [Resource] ] )
      : TileMessage [Measure] =
    other_tile .apply (
      message0
    ) (
      message1
    )

}

case class CrossFilterNotPLengthTile_ (p : Agent => Boolean) extends CrossFilterNotPLengthTile

object CrossFilterNotPLengthTile {
  def mk (p : Agent => Boolean) : CrossFilterNotPLengthTile =
    CrossFilterNotPLengthTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the group fairness pipeline.
 */

trait CrossFilterPLengthTile
{

  def   p : Agent => Boolean

  def to_number (a : Measure) : Number =
    a .getOrElse (-1)

  lazy val length_tile = LengthTile .mk [TilePair [Agent, Resource] ]

  lazy val cross_tile = CrossTile .mk [Agent, Resource]

  def apply (message0 : TileMessage [Seq [Agent] ] )  (message1 : TileMessage [Seq [Resource] ] )
      : TileMessage [Measure] =
    length_tile .apply (
      FilterTile .mk [TilePair [Agent, Resource] ] ( pair =>
        ! (OutcomeMod .mk .receives (message0 .outcome) (pair .fst)  (pair .snd) )
      ) .apply (
        cross_tile .apply (
          message0
        ) (
          message1
        )
      )
     )

}

case class CrossFilterPLengthTile_ (p : Agent => Boolean) extends CrossFilterPLengthTile

object CrossFilterPLengthTile {
  def mk (p : Agent => Boolean) : CrossFilterPLengthTile =
    CrossFilterPLengthTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This pipeline returns 'true' if there is group fairness, according to a hard-coded epsilon,
 * and 'false' otherwise.
 */

trait GroupFairnessPipeline
{

  def   p : Agent => Boolean

  lazy val epsilon : Number = 0.1

  def abs (a : Number) : Number =
    MathTool .mk .abs (a)

  def max (a : Number) (b : Number) : Number =
    MathTool .mk .max (a) (b)

  def length_to_number (a : Measure) : Number =
    a .getOrElse (-1)

  def divide (pair : TilePair [Measure, Measure] ) : Number =
    length_to_number (pair .fst) / length_to_number (pair .snd)

  def is_similar (a : Number) (b : Number) : Boolean =
    (a == b) ||
      (! (a == b) && ( (abs (a - b) / max (abs (a) ) (abs (b) ) ) < epsilon) )

  def is_pair_similar (pair : TilePair [Number, Number] ) : Boolean =
    is_similar (pair .fst) (pair .snd)

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val all_resource_tile = AllResourceTile .mk

  lazy val apply_divide_tile = ApplyTile .mk [TilePair [Measure, Measure] , Number] (divide)

  lazy val apply_similar_tile = ApplyTile .mk [TilePair [Number, Number] , Boolean] (is_pair_similar)

  lazy val cross_filter_p_length_tile = CrossFilterPLengthTile .mk (p)

  lazy val cross_filter_not_p_length_tile = CrossFilterNotPLengthTile .mk (p)

  lazy val all_agent_filter_p_length_tile = AllAgentFilterPLengthTile .mk (p)

  lazy val all_agent_filter_not_p_length_tile = AllAgentFilterNotPLengthTile .mk (p)

  lazy val tupling_pair_measure_tile = TuplingPairTile .mk [Measure, Measure]

  lazy val tupling_pair_number_tile = TuplingPairTile .mk [Number, Number]

  def pipeline_0 (message : TileMessage [Boolean] )
      : TileMessage [Measure] =
    cross_filter_p_length_tile .apply (
      all_agent_tile .apply (
        message
      )
    ) (
      all_resource_tile .apply (
        message
      )
    )

  def pipeline_1 (message : TileMessage [Boolean] ) : TileMessage [Number] =
    apply_divide_tile .apply (
      tupling_pair_measure_tile .apply (
        pipeline_0 (message)
      ) (
        all_agent_filter_p_length_tile .apply (
          message
        )
      )
    )

  def pipeline_2 (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    cross_filter_not_p_length_tile .apply (
      all_agent_tile .apply (
        message
      )
    ) (
      all_resource_tile .apply (
        message
      )
    )

  def pipeline_3 (message : TileMessage [Boolean] ) : TileMessage [Number] =
    apply_divide_tile .apply (
      tupling_pair_measure_tile .apply (
        pipeline_2 (message)
      ) (
        all_agent_filter_not_p_length_tile .apply (
          message
        )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_similar_tile .apply (
      tupling_pair_number_tile .apply (
        pipeline_1 (message)
      ) (
        pipeline_3 (message)
      )
    )

}

case class GroupFairnessPipeline_ (p : Agent => Boolean) extends GroupFairnessPipeline

object GroupFairnessPipeline {
  def mk (p : Agent => Boolean) : GroupFairnessPipeline =
    GroupFairnessPipeline_ (p)
}

