package soda.tiles.fairness.example.pipeline.complementginiindex

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.fold.LengthTile
import   soda.tiles.fairness.tile.derived.fold.SumTile
import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tile.primitive.CrossTile
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
 * This is a composite tile for the pipeline of the complement of the Gini index.
 */

trait AllAgentAccumulatesSumTile
{

  def   utility : Resource => Measure

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val sum_tile = SumTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    sum_tile .apply (
      accumulates_tile .apply (
        all_agent_tile .apply (
          message
        )
      )
    )

}

case class AllAgentAccumulatesSumTile_ (utility : Resource => Measure) extends AllAgentAccumulatesSumTile

object AllAgentAccumulatesSumTile {
  def mk (utility : Resource => Measure) : AllAgentAccumulatesSumTile =
    AllAgentAccumulatesSumTile_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the pipeline of the complement of the Gini index.
 */

trait AllAgentAccumulatesTile
{

  def   utility : Resource => Measure

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

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
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the pipeline of the complement of the Gini index.
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
 * This pipeline model the complement of the Gini index.
 */

trait ComplementGiniIndexPipeline
  extends
    Pipeline
{

  def   utility : Resource => Measure

  def to_number (a : Measure) : Number =
    a .getOrElse (-1)

  def phi (m3 : Measure) (m4 : Measure) (m5 : Measure) : Number =
    1 - (to_number (m3) / (2 * to_number (m5) * to_number (m4) ) )

  lazy val all_agent_accumulates_tile = AllAgentAccumulatesTile .mk (utility)

  lazy val all_agent_accumulates_sum_tile = AllAgentAccumulatesSumTile .mk (utility)

  lazy val all_agent_length_tile = AllAgentLengthTile .mk

  lazy val cross_map_sum_tile = CrossMapSumTile .mk

  lazy val tupling_pair_measure_tile = TuplingPairTile .mk [Measure, Measure]

  lazy val tupling_pair_pair_tile = TuplingPairTile .mk [TilePair [Measure, Measure] , Measure]

  lazy val apply_tile =
    ApplyTile .mk [TilePair [TilePair [Measure, Measure] , Measure] , Number] (
       tuple =>
        phi (tuple .fst .fst) (tuple .fst .snd) (tuple .snd)
    )

  def pipeline_0 (message : TileMessage [Boolean] ) : TileMessage [Measure] =
    cross_map_sum_tile .apply (
      all_agent_accumulates_tile .apply (
        message
      )
    ) (
      all_agent_accumulates_tile .apply (
        message
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Number] =
    apply_tile .apply (
      tupling_pair_pair_tile .apply (
        tupling_pair_measure_tile .apply (
          pipeline_0 .apply (
            message
          )
        ) (
          all_agent_accumulates_sum_tile .apply (
            message
          )
        )
      ) (
        all_agent_length_tile .apply (
          message
        )
      )
    )

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => apply (message)

}

case class ComplementGiniIndexPipeline_ (utility : Resource => Measure) extends ComplementGiniIndexPipeline

object ComplementGiniIndexPipeline {
  def mk (utility : Resource => Measure) : ComplementGiniIndexPipeline =
    ComplementGiniIndexPipeline_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the pipeline of the complement of the Gini index.
 */

trait CrossMapSumTile
{



  def phi (m0 : Measure) (m1 : Measure) : Measure =
    MeasureMod .mk .distance (m0) (m1)

  lazy val map_tile =
    MapTile .mk [TilePair [Measure, Measure] , Measure] (
       tuple =>
        phi (tuple .fst) (tuple .snd)
    )

  lazy val sum_tile = SumTile .mk

  lazy val cross_tile = CrossTile .mk [Measure, Measure]

  def apply (message0 : TileMessage [Seq [Measure] ] )  (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Measure] =
    sum_tile .apply (
      map_tile .apply (
        cross_tile .apply (
          message0
        ) (
          message1
        )
      )
    )

}

case class CrossMapSumTile_ () extends CrossMapSumTile

object CrossMapSumTile {
  def mk : CrossMapSumTile =
    CrossMapSumTile_ ()
}

