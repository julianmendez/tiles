package soda.tiles.fairness.tile.constant

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sorted sequence of agents, where each agent occurs exactly once.
 */

trait AllAgentTile
{



  def all_agents (message : TileMessage [Boolean] ) : Seq [Agent] =
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .agent)
        .distinct
        .sorted

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Agent] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      all_agents (message)
    )

}

case class AllAgentTile_ () extends AllAgentTile

object AllAgentTile {
  def mk : AllAgentTile =
    AllAgentTile_ ()
}

