package soda.tiles.fairness.tile.filter

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





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a possibly empty sequence of actors that satisfy a given property.
 */

trait FilterActorTile
{

  def   p : Actor => Boolean

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Actor] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .filter ( actor => p (actor) ) )
    )

}

case class FilterActorTile_ (p : Actor => Boolean) extends FilterActorTile

object FilterActorTile {
  def mk (p : Actor => Boolean) : FilterActorTile =
    FilterActorTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile returns a possibly empty sequence of measures that satisfy a given property.
 */

trait FilterMeasureTile
{

  def   p : Measure => Boolean

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .filter ( measure => p (measure) ) )
    )

}

case class FilterMeasureTile_ (p : Measure => Boolean) extends FilterMeasureTile

object FilterMeasureTile {
  def mk (p : Measure => Boolean) : FilterMeasureTile =
    FilterMeasureTile_ (p)
}

