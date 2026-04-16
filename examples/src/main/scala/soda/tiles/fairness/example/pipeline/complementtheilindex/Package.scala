package soda.tiles.fairness.example.pipeline.complementtheilindex

/*
 * This package contains classes to model the complement of the Theil index.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.fold.LengthTile
import   soda.tiles.fairness.tile.derived.fold.SumPhiTile
import   soda.tiles.fairness.tile.derived.fold.SumPhiNumberTile
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
import   soda.tiles.fairness.tool.TileTriple





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

  lazy val sum_tile = SumPhiTile .mk [Measure] ( x => x)

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

trait ComplementTheilIndexPipeline
  extends
    Pipeline
{

  def   utility : Resource => Measure

  lazy val all_agent_accumulates_tile = AllAgentAccumulatesTile .mk (utility)

  lazy val all_agent_accumulates_sum_tile = AllAgentAccumulatesSumTile .mk (utility)

  lazy val all_agent_length_tile = AllAgentLengthTile .mk

  lazy val tupling_pair_measure_tile = TuplingPairTile .mk [Measure, Measure]

  lazy val tupling_pair_pair_tile = TuplingPairTile .mk [TilePair [Measure, Measure] , Measure]

  lazy val map_sum_apply_tile = MapSumApplyTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Number] =
    map_sum_apply_tile .apply (
      all_agent_accumulates_tile .apply (
        message
      )
    ) (
      tupling_pair_measure_tile .apply (
        all_agent_accumulates_sum_tile .apply (
          message
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

case class ComplementTheilIndexPipeline_ (utility : Resource => Measure) extends ComplementTheilIndexPipeline

object ComplementTheilIndexPipeline {
  def mk (utility : Resource => Measure) : ComplementTheilIndexPipeline =
    ComplementTheilIndexPipeline_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the pipeline of the complement of the Theil index.
 */

trait MapSumApplyTile
{



  def to_number (a : Measure) : Number =
    a .getOrElse (-1)

  def apply_phi (m4 : Number) (m2 : Measure)  : Number =
    1 - (m4 / to_number (m2) )

  def log (x : Number) : Number =
    Math .log (x)

  def map_phi (m0 : Number) (m1 : Number) (m2 : Number) : Number =
    (m0 * m2 / m1) * log (m0 * m2 / m1)

  lazy val sum_tile = SumPhiNumberTile .mk [Number] ( x => x)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [TilePair [Measure, Measure] ] )
      : TileMessage [Number] =
    ApplyTile .mk [Number, Number] (
       value =>
         apply_phi (value) (message1 .contents .snd)
    ) .apply (
      sum_tile .apply (
        MapTile .mk [Measure, Number] (
          param =>
              map_phi (to_number (param) ) (to_number (message1 .contents .fst) ) (to_number (message1 .contents .snd) )
        ) .apply (
          message0
        )
      )
    )

}

case class MapSumApplyTile_ () extends MapSumApplyTile

object MapSumApplyTile {
  def mk : MapSumApplyTile =
    MapSumApplyTile_ ()
}

