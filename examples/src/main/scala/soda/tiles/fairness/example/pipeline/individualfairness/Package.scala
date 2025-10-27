package soda.tiles.fairness.example.pipeline.individualfairness

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.ForallTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.constant.AllResourceTile
import   soda.tiles.fairness.tile.primitive.CrossTile
import   soda.tiles.fairness.tile.primitive.FilterTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.OutcomeMod
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
 * This is a composite tile for the individual fairness pipeline.
 */

trait CrossFilterTile
{

  def   relevant_attribute : Agent => Boolean

  lazy val cross_tile = CrossTile .mk [Agent, Agent]

  def filter_function_pair (a0 : Agent) (a1 : Agent) : Boolean =
    ! (a0 == a1) && (relevant_attribute (a0) == relevant_attribute (a1) )

  def filter_function (pair : TilePair [Agent, Agent] ) : Boolean =
    filter_function_pair (pair .fst) (pair .snd)

  lazy val filter_tile = FilterTile .mk [TilePair [Agent, Agent] ] (filter_function)

  def apply (message0 : TileMessage [Seq [Agent] ] )  (message1 : TileMessage [Seq [Agent] ] )
      : TileMessage [Seq [TilePair [Agent, Agent] ] ] =
    filter_tile .apply (
      cross_tile .apply (
        message0
      ) (
        message1
      )
    )

}

case class CrossFilterTile_ (relevant_attribute : Agent => Boolean) extends CrossFilterTile

object CrossFilterTile {
  def mk (relevant_attribute : Agent => Boolean) : CrossFilterTile =
    CrossFilterTile_ (relevant_attribute)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This is a composite tile for the individual fairness pipeline.
 */

trait CrossForallTile
{



  def receives (outcome : Outcome) (a : Agent) (r : Resource) : Boolean =
    OutcomeMod .mk .receives (outcome) (a) (r)

  def has_condition (outcome : Outcome) (a0 : Agent) (a1 : Agent) (r : Resource) : Boolean =
     (receives (outcome) (a0) (r) && receives (outcome) (a1) (r) ) ||
       (! (receives (outcome) (a0) (r) ) && ! (receives (outcome) (a1) (r) ) )

  lazy val cross_tile = CrossTile .mk [TilePair [Agent, Agent] , Resource]

  def apply (message0 : TileMessage [Seq [TilePair [Agent, Agent] ] ] )  (message1 : TileMessage [Seq [Resource] ] )
      : TileMessage [Boolean] =
    ForallTile .mk [TilePair [TilePair [Agent, Agent] , Resource] ] ( pair =>
      has_condition (message0 .outcome) (pair .fst .fst) (pair .fst .snd) (pair .snd)
    ) .apply (
      cross_tile .apply (
        message0
      ) (
        message1
      )
    )

}

case class CrossForallTile_ () extends CrossForallTile

object CrossForallTile {
  def mk : CrossForallTile =
    CrossForallTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.constant.AllAgentTile
import Soda.tiles.fairness.tile.composite.AllEqualTile
import Soda.tiles.fairness.tile.composite.ReceivedSigmaPTile
*/

/**
 * This pipeline returns 'true' if there is individual fairness, and 'false' otherwise.
 */

trait IndividualFairnessPipeline
  extends
    Pipeline
{

  def   relevant_attribute : Agent => Boolean

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val all_resource_tile = AllResourceTile .mk

  lazy val cross_filter_tile = CrossFilterTile .mk (relevant_attribute)

  lazy val cross_forall_tile = CrossForallTile .mk

  def pipeline_0 (message : TileMessage [Boolean] ) : TileMessage [Seq [TilePair [Agent, Agent] ] ] =
    cross_filter_tile .apply (
      all_agent_tile .apply (
        message
      )
    ) (
      all_agent_tile .apply (
        message
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    cross_forall_tile .apply (
      pipeline_0 (message)
    ) (
      all_resource_tile .apply (
        message
      )
    )

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => as_number (apply (message) )

}

case class IndividualFairnessPipeline_ (relevant_attribute : Agent => Boolean) extends IndividualFairnessPipeline

object IndividualFairnessPipeline {
  def mk (relevant_attribute : Agent => Boolean) : IndividualFairnessPipeline =
    IndividualFairnessPipeline_ (relevant_attribute)
}

