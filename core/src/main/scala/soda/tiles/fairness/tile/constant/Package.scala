package soda.tiles.fairness.tile.constant

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Resource
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


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sorted sequence of resources, where each resource occurs exactly once.
 */

trait AllResourceTile
{



  def all_resources (message : TileMessage [Boolean] ) : Seq [Resource] =
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .resource)
        .distinct
        .sorted

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Resource] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      all_resources (message)
    )

}

case class AllResourceTile_ () extends AllResourceTile

object AllResourceTile {
  def mk : AllResourceTile =
    AllResourceTile_ ()
}

