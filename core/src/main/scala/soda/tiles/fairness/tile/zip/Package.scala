package soda.tiles.fairness.tile.zip

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tile.map.SigmaTile

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


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects two sequences and returns a sequence of pairs, such that for each
 * position in both sequences, it has a pair with elements for the corresponding input
 * sequences.
 */

trait ZipTile
{



  def zip_lists [A , B ] (list0 : Seq [A] ) (list1 : Seq [B] )
      : Seq [TilePair [A, B] ] =
    list0
      .zip (list1)
      .map ( pair => TilePair .mk [A, B] (pair ._1) (pair ._2) )

  def apply [A , B ] (message0 : TileMessage [Seq [A] ] )
      (message1 : TileMessage [Seq [B] ] ) : TileMessage [Seq [TilePair [A, B] ] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      zip_lists (message0 .contents) (message1 .contents)
    )

}

case class ZipTile_ () extends ZipTile

object ZipTile {
  def mk : ZipTile =
    ZipTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile connects three sequences and returns a sequence of triples, such that for each
 * position in both sequences, it has a triple with elements for the corresponding input
 * sequences.
 */

trait ZipTripleTile
{



  def zip_lists [A , B , C ] (list0 : Seq [A] ) (list1 : Seq [B] ) (list2 : Seq [C] )
      : Seq [TileTriple [A, B, C] ] =
    list0
      .zip (list1)
      .zip (list2)
      .map ( triple => TileTriple .mk [A, B, C] (triple ._1 ._1) (triple ._1 ._2) (triple ._2) )

  def apply [A , B , C ] (message0 : TileMessage [Seq [A] ] )
    (message1 : TileMessage [Seq [B] ] ) (message2 : TileMessage [Seq [C] ] )
      : TileMessage [Seq [TileTriple [A, B, C] ] ] =
    TileMessageBuilder .mk .build (message0 .context) (message0 .outcome) (
      zip_lists (message0 .contents) (message1 .contents) (message2 .contents)
    )

}

case class ZipTripleTile_ () extends ZipTripleTile

object ZipTripleTile {
  def mk : ZipTripleTile =
    ZipTripleTile_ ()
}

