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
import   soda.tiles.fairness.tile.constant.TuplingPairTile
import   soda.tiles.fairness.tile.constant.TuplingTripleTile
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


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects two elements and returns a pair.
 */

trait TuplingPairTile
{



  def apply [A , B ] (message0 : TileMessage [A] )
      (message1 : TileMessage [B] ) : TileMessage [TilePair [A, B] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      TilePair .mk [A, B] (message0 .contents) (message1 .contents)
    )

}

case class TuplingPairTile_ () extends TuplingPairTile

object TuplingPairTile {
  def mk : TuplingPairTile =
    TuplingPairTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects three elements and returns a triple.
 */

trait TuplingTripleTile
{



  def apply [A , B , C ] (message0 : TileMessage [A] )
    (message1 : TileMessage [B] ) (message2 : TileMessage [C] )
      : TileMessage [TileTriple [A, B, C] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      TileTriple .mk [A, B, C] (message0 .contents) (message1 .contents) (message2 .contents)
    )

}

case class TuplingTripleTile_ () extends TuplingTripleTile

object TuplingTripleTile {
  def mk : TuplingTripleTile =
    TuplingTripleTile_ ()
}

