package soda.tiles.fairness.tile.zip

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tile.core.ZipTile
import   soda.tiles.fairness.tile.map.SigmaTile
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes two sequences of measures as input, and returns a sequence such that,
 * for each pair (m0, m1) in the input, is m = sigma (m0, m1), where sigma is a given function
 * to combine measures.
 */

trait ZipSigmaTile
{

  def   sigma : Measure => Measure => Measure

  lazy val zip_tile = ZipTile .mk

  lazy val sigma_tile = SigmaTile .mk (sigma)

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] )
      : TileMessage [Seq [Measure] ] =
    sigma_tile .apply (
      zip_tile .apply (message0) (message1)
    )

}

case class ZipSigmaTile_ (sigma : Measure => Measure => Measure) extends ZipSigmaTile

object ZipSigmaTile {
  def mk (sigma : Measure => Measure => Measure) : ZipSigmaTile =
    ZipSigmaTile_ (sigma)
}

