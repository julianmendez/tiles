package soda.tiles.fairness.example.pipeline.jainsindex

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.fold.LengthTile
import   soda.tiles.fairness.tile.derived.fold.SumTile
import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tile.primitive.TuplingPairTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.MeasureMod
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tool.Pipeline
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TilePair





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the Jain's index pipeline.
 */

trait AllAgentAccumulatesMapSumTile
{

  def   utility : Resource => Measure

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val map_tile = MapTile .mk [Measure, Measure] (MeasureMod .mk .squared)

  lazy val sum_tile = SumTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    sum_tile .apply (
      map_tile .apply (
        accumulates_tile .apply (
          all_agent_tile .apply (
            message
          )
        )
      )
    )

}

case class AllAgentAccumulatesMapSumTile_ (utility : Resource => Measure) extends AllAgentAccumulatesMapSumTile

object AllAgentAccumulatesMapSumTile {
  def mk (utility : Resource => Measure) : AllAgentAccumulatesMapSumTile =
    AllAgentAccumulatesMapSumTile_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the Jain's index pipeline.
 */

trait AllAgentAccumulatesSumApplyTile
{

  def   utility : Resource => Measure

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val sum_tile = SumTile .mk

  lazy val apply_tile = ApplyTile .mk [Measure, Measure] (MeasureMod .mk .squared)

  def apply (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    apply_tile .apply (
      sum_tile .apply (
        accumulates_tile .apply (
          all_agent_tile .apply (
            message
          )
        )
      )
    )

}

case class AllAgentAccumulatesSumApplyTile_ (utility : Resource => Measure) extends AllAgentAccumulatesSumApplyTile

object AllAgentAccumulatesSumApplyTile {
  def mk (utility : Resource => Measure) : AllAgentAccumulatesSumApplyTile =
    AllAgentAccumulatesSumApplyTile_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the group fairness pipeline.
 */

trait AllAgentLengthTile
{



  lazy val all_agent_tile = AllAgentTile .mk

  lazy val length_tile = LengthTile .mk [Agent]

  def apply (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    length_tile .apply (
      all_agent_tile .apply (
        message
      )
    )

}

case class AllAgentLengthTile_ () extends AllAgentLengthTile

object AllAgentLengthTile {
  def mk : AllAgentLengthTile =
    AllAgentLengthTile_ ()
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

trait JainsIndexPipeline
  extends
    Pipeline
{

  def   utility : Resource => Measure

  def to_number (a : Measure) : Number =
    a .getOrElse (-1)

  def phi (m0 : Measure) (m1 : Measure) (m2 : Measure) : Number =
    to_number (m0) / (to_number (m1) * to_number (m2) )

  lazy val all_agent_accumulates_map_sum_tile = AllAgentAccumulatesMapSumTile .mk (utility)

  lazy val all_agent_accumulates_sum_apply_tile = AllAgentAccumulatesSumApplyTile .mk (utility)

  lazy val all_agent_length_tile = AllAgentLengthTile .mk

  lazy val tupling_pair_measure_tile = TuplingPairTile .mk [Measure, Measure]

  lazy val tupling_pair_pair_tile = TuplingPairTile .mk [TilePair [Measure, Measure] , Measure]

  lazy val apply_tile =
    ApplyTile .mk [TilePair [TilePair [Measure, Measure] , Measure] , Number] (
       tuple =>
        phi (tuple .fst .fst) (tuple .fst .snd) (tuple .snd)
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Number] =
    apply_tile .apply (
      tupling_pair_pair_tile .apply (
        tupling_pair_measure_tile .apply (
          all_agent_accumulates_map_sum_tile .apply (message)
        ) (
          all_agent_accumulates_sum_apply_tile .apply (message)
        )
      ) (
        all_agent_length_tile .apply (message)
      )
    )

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => apply (message)

}

case class JainsIndexPipeline_ (utility : Resource => Measure) extends JainsIndexPipeline

object JainsIndexPipeline {
  def mk (utility : Resource => Measure) : JainsIndexPipeline =
    JainsIndexPipeline_ (utility)
}

