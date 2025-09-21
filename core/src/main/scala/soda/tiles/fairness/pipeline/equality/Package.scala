package soda.tiles.fairness.pipeline.equality

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.composite.AllEqualTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
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

