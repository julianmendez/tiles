package soda.tiles.fairness.pipeline.envyfreeness

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
import   soda.tiles.fairness.tile.derived.map.NeedsTile
import   soda.tiles.fairness.tile.primitive.ApplyTile
import   soda.tiles.fairness.tile.primitive.CrossTile
import   soda.tiles.fairness.tile.primitive.FilterTile
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
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the envy freeness pipelines.
 */

trait CrossExistsTile
{

  def   preference : Preference

  def evaluate_exists (outcome : Outcome) (a : Agent) (r0 : Resource) (r1 : Resource) : Boolean =
    ! (r1 == r0) && (OutcomeMod .mk .receives (outcome) (a) (r1) ) &&
      ! (PreferenceMod .mk .prefers (preference) (a) (r0) (r1) )

  lazy val cross_tile = CrossTile .mk [TilePair [Agent, Resource] , Resource]

  def apply (message0 : TileMessage [Seq [TilePair [Agent, Resource] ] ] ) (message1 : TileMessage [Seq [Resource] ] )
      : TileMessage [Boolean] =
    ExistsTile .mk [TilePair [TilePair [Agent, Resource] , Resource] ] ( pair =>
      evaluate_exists (message0 .outcome) (pair .fst .fst) (pair .fst .snd) (pair .snd)
    ) .apply (
      cross_tile .apply (
        message0
      ) (
        message1
      )
    )

}

case class CrossExistsTile_ (preference : Preference) extends CrossExistsTile

object CrossExistsTile {
  def mk (preference : Preference) : CrossExistsTile =
    CrossExistsTile_ (preference)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the envy freeness pipelines.
 */

trait CrossFilterMapTile
{



  lazy val map_tile = MapTile .mk [TilePair [Agent, Resource] , Resource] (
     pair => pair .snd
  )

  lazy val cross_tile = CrossTile .mk [Agent, Resource]

  def apply (message0 : TileMessage [Seq [Agent] ] )  (message1 : TileMessage [Seq [Resource] ] )
      : TileMessage [Seq [Resource] ] =
    map_tile .apply (
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

case class CrossFilterMapTile_ () extends CrossFilterMapTile

object CrossFilterMapTile {
  def mk : CrossFilterMapTile =
    CrossFilterMapTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the envy freeness pipelines.
 */

trait CrossFilterTile
{



  lazy val cross_tile = CrossTile .mk [Agent, Resource]

  def apply (message0 : TileMessage [Seq [Agent] ] )  (message1 : TileMessage [Seq [Resource] ] )
      : TileMessage [Seq [TilePair [Agent, Resource] ] ] =
    FilterTile .mk [TilePair [Agent, Resource] ] ( pair =>
      ! (OutcomeMod .mk .receives (message0 .outcome) (pair .fst)  (pair .snd) )
    ) .apply (
      cross_tile .apply (
        message0
      ) (
        message1
      )
    )

}

case class CrossFilterTile_ () extends CrossFilterTile

object CrossFilterTile {
  def mk : CrossFilterTile =
    CrossFilterTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This pipeline returns 'true' when all the agents in the input receive
 * at least one resource that is better than any other resource
 * received by any other agent.
 */

trait EnvyFreenessPipeline
{

  def   preference : Preference

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val all_resource_tile = AllResourceTile .mk

  lazy val cross_filter_map_tile = CrossFilterMapTile .mk

  lazy val cross_filter_tile = CrossFilterTile .mk

  lazy val cross_exists_tile = CrossExistsTile .mk (preference)

  def pipeline_0 (message : TileMessage [Boolean] )
      : TileMessage [Seq [Resource] ] =
    cross_filter_map_tile .apply (
      all_agent_tile .apply (
        message
      )
    ) (
      all_resource_tile .apply (
        message
      )
    )

  def pipeline_1 (message : TileMessage [Boolean] )
      : TileMessage [Seq [TilePair [Agent, Resource] ] ] =
    cross_filter_tile .apply (
      all_agent_tile .apply (
        message
      )
    ) (
      pipeline_0 (
        message
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    cross_exists_tile .apply (
      pipeline_1 .apply (
        message
      )
    ) (
      all_resource_tile .apply (
        message
      )
    )

}

case class EnvyFreenessPipeline_ (preference : Preference) extends EnvyFreenessPipeline

object EnvyFreenessPipeline {
  def mk (preference : Preference) : EnvyFreenessPipeline =
    EnvyFreenessPipeline_ (preference)
}


trait Preference
{

  def    map : Map [Agent, Seq [Resource] ]

}

case class Preference_ (map : Map [Agent, Seq [Resource] ]) extends Preference

object Preference {
  def mk (map : Map [Agent, Seq [Resource] ]) : Preference =
    Preference_ (map)
}

trait PreferenceMod
{



  def get_preferences (preference : Preference) (a : Agent) : Seq [Resource] =
    preference .map .get (a) .getOrElse (Seq [Resource] () )

  private def _occurs_before (r0_index : Int) (r1_index : Int) : Boolean =
    (r0_index >= 0) && (r1_index >=0) && (r0_index < r1_index)

  private def _prefers_with (seq : Seq [Resource] ) (r0 : Resource) (r1 : Resource) : Boolean =
    _occurs_before (seq .indexOf (r0) ) (seq .indexOf (r1) )

  /**
   * Considering a sequence of resources 'seq', an agent 'a' prefers
   * a resource 'r0' over another resource 'r1' if and only if
   * provided that 'r0' is in 'seq' and 'r1' is in 'seq', then 'r0' is before 'r1' in 'seq'.
   */

  def prefers (preference : Preference) (a : Agent) (r0 : Resource) (r1 : Resource) : Boolean =
    _prefers_with (get_preferences (preference) (a) ) (r0) (r1)

}

case class PreferenceMod_ () extends PreferenceMod

object PreferenceMod {
  def mk : PreferenceMod =
    PreferenceMod_ ()
}

