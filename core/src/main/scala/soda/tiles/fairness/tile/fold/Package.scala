package soda.tiles.fairness.tile.fold

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tile.core.ZipTile





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of pairs of measures, and compares both components (m0, m1).
 * If for each pair (m0, m1), it holds that m0 >= m1, it returns 'true, otherwise, it returns
 * 'false'.
 */

trait AllAtLeastTile
{



  lazy val zip_tile = ZipTile .mk

  def apply_zipped (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( pair =>
          (Comparator .mk
            .compareMeasure (pair .fst) (pair .snd) ) >= 0 )
        .forall ( e => e)
      )
    )

  def apply (message0 : TileMessage [Seq [Measure] ] ) (message1 : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    apply_zipped (
      zip_tile .apply [Measure, Measure] (message0) (message1)
    )

}

case class AllAtLeastTile_ () extends AllAtLeastTile

object AllAtLeastTile {
  def mk : AllAtLeastTile =
    AllAtLeastTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of measures and returns 'true' when all measures are the same as
 * the first one.
 */

trait AllEqual1Tile
{



  def apply (message : TileMessage [Seq [Measure] ] ) (list : Seq [Measure] ) : Boolean =
    list match  {
      case Nil => true
      case (x :: xs) => xs .forall ( e => x == e)
    }

}

case class AllEqual1Tile_ () extends AllEqual1Tile

object AllEqual1Tile {
  def mk : AllEqual1Tile =
    AllEqual1Tile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllEqual1Tile
*/

/**
 * This tile takes a sequence of measures and returns 'true' when they are all the same.
 */

trait AllEqualTile
{



  lazy val all_equal_1_tile = AllEqual1Tile .mk

  def apply (message : TileMessage [Seq [Measure] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      all_equal_1_tile .apply (message) (message .contents)
    )

}

case class AllEqualTile_ () extends AllEqualTile

object AllEqualTile {
  def mk : AllEqualTile =
    AllEqualTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of elements and returns 'true' when at least one element in the input
 * satisfies a property.
 */

trait ExistsTile [A]
{

  def   p : A => Boolean

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .exists ( elem => p (elem) ) )
    )

}

case class ExistsTile_ [A] (p : A => Boolean) extends ExistsTile [A]

object ExistsTile {
  def mk [A] (p : A => Boolean) : ExistsTile [A] =
    ExistsTile_ [A] (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of elements and returns 'true' when all the elements in the input
 * satisfy a property.
 */

trait ForallTile [A]
{

  def   p : A => Boolean

  def apply (message : TileMessage [Seq [A] ] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .forall ( elem => p (elem) ) )
    )

}

case class ForallTile_ [A] (p : A => Boolean) extends ForallTile [A]

object ForallTile {
  def mk [A] (p : A => Boolean) : ForallTile [A] =
    ForallTile_ [A] (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This tile takes a sequence of agents as input and returns a sequence containing, for each
 * agent in the input sequence, a measure amounting the value of all the resources given to that
 * agent. This tile requires a function to count multiple resources, and another function that
 * informs the value of each resource.
 */

trait ReceivedSigmaPTile
{

  def   sigma : Measure => Measure => Measure
  def   p : Resource => Measure

  private def _sigma2 (m0 : Measure , m1 : Measure) : Measure =
    sigma (m0) (m1)

  private lazy val _measure_zero : Measure = Some (0)

  def get_assignment (assignments : Seq [Assignment] ) (a : Agent) : Option [Assignment] =
    assignments . find ( assignment => (assignment .agent) == a)

  def get_measure (outcome : Outcome) (a : Agent) : Measure =
    (get_assignment (outcome .assignments) (a) )
      .map ( assignment => p (assignment .resource) )
      .foldLeft (_measure_zero) (_sigma2)

  def apply (message : TileMessage [Seq [Agent] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( agent => get_measure (message .outcome) (agent) ) )
    )

}

case class ReceivedSigmaPTile_ (sigma : Measure => Measure => Measure, p : Resource => Measure) extends ReceivedSigmaPTile

object ReceivedSigmaPTile {
  def mk (sigma : Measure => Measure => Measure) (p : Resource => Measure) : ReceivedSigmaPTile =
    ReceivedSigmaPTile_ (sigma, p)
}

