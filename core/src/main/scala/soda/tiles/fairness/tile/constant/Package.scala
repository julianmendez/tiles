package soda.tiles.fairness.tile.constant

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Actor
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
 * This tile returns a sequence of pairs containing the same actor, sorted by actor, where each
 * pair of actors occurs exactly once.
 */

trait AllActorPairTile
{



  lazy val zip_tile = TuplingPairTile .mk

  lazy val all_actor_tile = AllActorTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [TilePair [Seq [Actor] , Seq [Actor] ] ] =
    zip_tile .apply (all_actor_tile (message) ) (all_actor_tile (message) )

}

case class AllActorPairTile_ () extends AllActorPairTile

object AllActorPairTile {
  def mk : AllActorPairTile =
    AllActorPairTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sorted sequence of actors, where each actor occurs exactly once.
 */

trait AllActorTile
{



  def all_actors [A ] (message : TileMessage [A] ) : Seq [Actor] =
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted

  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Actor] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      all_actors [Boolean] (message)
    )

}

case class AllActorTile_ () extends AllActorTile

object AllActorTile {
  def mk : AllActorTile =
    AllActorTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a sequence of triples containing the same actor, sorted by actor, where
 * each triple of actors occurs exactly once.
 */

trait AllActorTripleTile
{



  lazy val zip_tile = TuplingTripleTile .mk

  lazy val all_actor_tile = AllActorTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [TileTriple [Seq [Actor] , Seq [Actor] , Seq [Actor] ] ] =
    zip_tile .apply (all_actor_tile (message) ) (all_actor_tile (message) ) (all_actor_tile (message) )

}

case class AllActorTripleTile_ () extends AllActorTripleTile

object AllActorTripleTile {
  def mk : AllActorTripleTile =
    AllActorTripleTile_ ()
}

