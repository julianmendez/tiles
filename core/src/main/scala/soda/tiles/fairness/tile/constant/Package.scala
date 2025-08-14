package soda.tiles.fairness.tile.constant

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Pearson
import   soda.tiles.fairness.tool.PearsonMod
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TilePair_
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tool.TileTriple_
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tile.apply.TuplingPairTile
import   soda.tiles.fairness.tile.apply.TuplingTripleTile
import   soda.tiles.fairness.tile.zip.ZipPairTile

/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sequence of pairs containing the same agent, sorted by agent, where each
 * pair of agents occurs exactly once.
 */

trait AllAgentPairTile
{



  lazy val zip_tile = TuplingPairTile .mk

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] =
    zip_tile .apply (all_agent_tile (message) ) (all_agent_tile (message) )

}

case class AllAgentPairTile_ () extends AllAgentPairTile

object AllAgentPairTile {
  def mk : AllAgentPairTile =
    AllAgentPairTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sorted sequence of agents, where each agent occurs exactly once.
 */

trait AllAgentTile
{



  def all_agents [A ] (message : TileMessage [A] ) : Seq [Agent] =
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .agent)
        .distinct
        .sorted

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Agent] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      all_agents [Boolean] (message)
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
 * This tile returns a sequence of triples containing the same agent, sorted by agent, where
 * each triple of agents occurs exactly once.
 */

trait AllAgentTripleTile
{



  lazy val zip_tile = TuplingTripleTile .mk

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [TileTriple [Seq [Agent] , Seq [Agent] , Seq [Agent] ] ] =
    zip_tile .apply (all_agent_tile (message) ) (all_agent_tile (message) ) (all_agent_tile (message) )

}

case class AllAgentTripleTile_ () extends AllAgentTripleTile

object AllAgentTripleTile {
  def mk : AllAgentTripleTile =
    AllAgentTripleTile_ ()
}

