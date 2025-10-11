package soda.tiles.fairness.pipeline.equity

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.composite.AllAtLeastTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.map.NeedsTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Measure
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

