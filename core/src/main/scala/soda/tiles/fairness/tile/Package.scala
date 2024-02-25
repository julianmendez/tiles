package soda.tiles.fairness.tile

/*
 * This package contains classes to model the tiles.
 */

import   soda.tiles.fairness.tool.Actor
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Comparator
import   soda.tiles.fairness.tool.Comparator_
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Pearson
import   soda.tiles.fairness.tool.Pearson_
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tool.TilePair_
import   soda.tiles.fairness.tool.TileTriple
import   soda.tiles.fairness.tool.TileTriple_





/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait AllActorPairTile
{



  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [TilePair [Actor, Actor] ] ] =
    TileMessageBuilder .mk .build (
      message .context) (message .outcome) ( ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted
        .map ( actor => TilePair .mk (actor) (actor) )
    )

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

trait AllActorTile
{



  def apply (message : TileMessage [Boolean] ) : TileMessage [Seq [Actor] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted
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

trait AllActorTripleTile
{



  def apply (message : TileMessage [Boolean] )
      : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .outcome) .assignments)
        .map ( assignment => assignment .actor)
        .distinct
        .sorted
        .map ( actor => TileTriple .mk (actor) (actor) (actor) )
    )

}

case class AllActorTripleTile_ () extends AllActorTripleTile

object AllActorTripleTile {
  def mk : AllActorTripleTile =
    AllActorTripleTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
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

trait AtLeastTile
{



  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( pair =>
          (Comparator .mk
            .compareMeasure (pair .fst) (pair .snd) ) >= 0 )
        .forall ( e => e)
      )
    )

}

case class AtLeastTile_ () extends AtLeastTile

object AtLeastTile {
  def mk : AtLeastTile =
    AtLeastTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait AttributePTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( actor => p (actor) ) )
    )

}

case class AttributePTile_ (p : Actor => Measure) extends AttributePTile

object AttributePTile {
  def mk (p : Actor => Measure) : AttributePTile =
    AttributePTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait CorrelationTile
{



  private lazy val _measure_zero : Measure = Some (0)

  private lazy val _percentage_constant : Float = 100.0

  def get_coefficient (xlist : Seq [Float] ) (ylist : Seq [Float] ) : Float =
    (Pearson .mk (xlist) (ylist) ) .coefficient

  def to_double (m : Measure) : Float =
    if ( (m == _measure_zero)
    ) 0.0
    else 1.0

  def to_measure (d : Float) : Measure =
    Some ( (d * _percentage_constant) .intValue)

  def get_fst_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Float] =
    lists .map ( pair => to_double (pair .fst) )

  def get_snd_list (lists : Seq [TilePair [Measure, Measure] ] ) : Seq [Float] =
    lists .map ( pair => to_double (pair .snd) )

  def process_tuples (lists : Seq [TilePair [Measure, Measure] ] ) : Measure =
    to_measure (get_coefficient (get_fst_list (lists) ) (get_snd_list (lists) ) )

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Measure] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      process_tuples (message .contents)
    )

}

case class CorrelationTile_ () extends CorrelationTile

object CorrelationTile {
  def mk : CorrelationTile =
    CorrelationTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait DecisionTile
{

  def   maximum_acceptable_bias_percentage : Measure

  def to_boolean (m : Measure) : Boolean =
    ( Comparator .mk
       .compareMeasure (m) (maximum_acceptable_bias_percentage) ) <= 0

  def apply (message : TileMessage [Measure] ) : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      to_boolean (message .contents)
    )

}

case class DecisionTile_ (maximum_acceptable_bias_percentage : Measure) extends DecisionTile

object DecisionTile {
  def mk (maximum_acceptable_bias_percentage : Measure) : DecisionTile =
    DecisionTile_ (maximum_acceptable_bias_percentage)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllActorTile
import Soda.tiles.fairness.tile.AllEqualTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
*/

trait EqualityTile
{

  def   sigma : Measure => Measure => Measure
  def   p_utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p_utility)

  lazy val all_actor_tile = AllActorTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        all_actor_tile .apply (message)
      )
    )

}

case class EqualityTile_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure) extends EqualityTile

object EqualityTile {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) : EqualityTile =
    EqualityTile_ (sigma, p_utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AtLeastTile
import Soda.tiles.fairness.tile.NeededPTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
import Soda.tiles.fairness.tile.UnzipPairFstTile
import Soda.tiles.fairness.tile.UnzipPairSndTile
import Soda.tiles.fairness.tile.ZipTile
*/

trait EquityTile
{

  def   sigma : Measure => Measure => Measure
  def   p0_need : Actor => Measure
  def   p1_utility : Resource => Measure

  lazy val at_least_tile = AtLeastTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p1_utility)

  lazy val needed_p_tile = NeededPTile .mk (p0_need)

  lazy val all_actor_pair_tile = AllActorPairTile .mk

  lazy val unzip_fst_tile = UnzipPairFstTile .mk

  lazy val unzip_snd_tile = UnzipPairSndTile .mk

  lazy val zip_tile = ZipTile .mk

  def get_branch_0 (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    received_sigma_p_tile .apply (unzip_fst_tile .apply (message) )

  def get_branch_1 (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    needed_p_tile .apply (unzip_snd_tile .apply (message) )

  def zip_branches (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Seq [TilePair [Measure, Measure] ] ] =
    zip_tile .apply (get_branch_0 (message) ) (get_branch_1 (message) )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    at_least_tile .apply (
      zip_branches (
        all_actor_pair_tile .apply (message)
      )
    )

}

case class EquityTile_ (sigma : Measure => Measure => Measure, p0_need : Actor => Measure, p1_utility : Resource => Measure) extends EquityTile

object EquityTile {
  def mk (sigma : Measure => Measure => Measure) (p0_need : Actor => Measure) (p1_utility : Resource => Measure) : EquityTile =
    EquityTile_ (sigma, p0_need, p1_utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.SigmaTile
*/

trait FalsePosTile
{



  private lazy val _measure_zero : Measure = Some (0)

  private lazy val _measure_one : Measure = Some (1)

  def sigma (m0 : Measure) (m1 : Measure) : Measure =
    if ( (m0 == _measure_one) && (m1 == _measure_zero)
    ) _measure_one
    else _measure_zero

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
      : TileMessage [Seq [Measure] ] =
    SigmaTile .mk (sigma) .apply (message)

}

case class FalsePosTile_ () extends FalsePosTile

object FalsePosTile {
  def mk : FalsePosTile =
    FalsePosTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AttributePTile
*/

trait NeededPTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    AttributePTile .mk (p) .apply (message)

}

case class NeededPTile_ (p : Actor => Measure) extends NeededPTile

object NeededPTile {
  def mk (p : Actor => Measure) : NeededPTile =
    NeededPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
*/

trait PredictionPTile
{

  def   p : Resource => Measure

  private lazy val _measure_zero : Measure = Some (0)

  def measure_or (m0 : Measure) (m1 : Measure) : Measure =
    if ( (m0 == _measure_zero)
    ) m1
    else m0

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    (ReceivedSigmaPTile .mk (measure_or) (p) ) .apply (message)

}

case class PredictionPTile_ (p : Resource => Measure) extends PredictionPTile

object PredictionPTile {
  def mk (p : Resource => Measure) : PredictionPTile =
    PredictionPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait ReceivedSigmaPTile
{

  def   sigma : Measure => Measure => Measure
  def   p : Resource => Measure

  private def _sigma2 (m0 : Measure , m1 : Measure) : Measure =
    sigma (m0) (m1)

  private lazy val _measure_zero : Measure = Some (0)

  def get_assignment (assignments : Seq [Assignment] ) (a : Actor) : Option [Assignment] =
    assignments . find ( assignment => (assignment .actor) == a)

  def get_measure (outcome : Outcome) (a : Actor) : Measure =
    (get_assignment (outcome .assignments) (a) )
      .map ( assignment => p (assignment .resource) )
      .foldLeft (_measure_zero) (_sigma2)

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      ( (message .contents)
        .map ( actor => get_measure (message .outcome) (actor) ) )
    )

}

case class ReceivedSigmaPTile_ (sigma : Measure => Measure => Measure, p : Resource => Measure) extends ReceivedSigmaPTile

object ReceivedSigmaPTile {
  def mk (sigma : Measure => Measure => Measure) (p : Resource => Measure) : ReceivedSigmaPTile =
    ReceivedSigmaPTile_ (sigma, p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AttributePTile
*/

trait ResultPTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    AttributePTile .mk (p) .apply (message)

}

case class ResultPTile_ (p : Actor => Measure) extends ResultPTile

object ResultPTile {
  def mk (p : Actor => Measure) : ResultPTile =
    ResultPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait SigmaTile
{

  def   sigma : Measure => Measure => Measure

  def apply (message : TileMessage [Seq [TilePair [Measure, Measure] ] ] )
    : TileMessage [Seq [Measure] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      (message .contents)
        .map ( pair => sigma (pair .fst) (pair .snd) )
    )

}

case class SigmaTile_ (sigma : Measure => Measure => Measure) extends SigmaTile

object SigmaTile {
  def mk (sigma : Measure => Measure => Measure) : SigmaTile =
    SigmaTile_ (sigma)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AllActorTripleTile
import Soda.tiles.fairness.tile.CorrelationTile
import Soda.tiles.fairness.tile.DecisionTile
import Soda.tiles.fairness.tile.FalsePosTile
import Soda.tiles.fairness.tile.PredictionPTile
import Soda.tiles.fairness.tile.ResultPTile
import Soda.tiles.fairness.tile.UnzipTripleFstTile
import Soda.tiles.fairness.tile.UnzipTripleSndTile
import Soda.tiles.fairness.tile.UnzipTripleTrdTile
import Soda.tiles.fairness.tile.WithPTile
import Soda.tiles.fairness.tile.ZipTile
*/

trait UnbiasednessTile
{

  def   p0_evaluation : Resource => Measure
  def   p1_result : Actor => Measure
  def   p2_with_p : Actor => Measure
  def   p3_acceptable_bias : Measure

  lazy val all_actor_triple_tile = AllActorTripleTile .mk

  lazy val unzip_fst_tile = UnzipTripleFstTile .mk

  lazy val unzip_snd_tile = UnzipTripleSndTile .mk

  lazy val unzip_trd_tile = UnzipTripleTrdTile .mk

  lazy val zip_tile = ZipTile .mk

  lazy val prediction_p_tile = PredictionPTile .mk (p0_evaluation)

  lazy val result_p_tile = ResultPTile .mk (p1_result)

  lazy val with_p_tile = WithPTile .mk (p2_with_p)

  lazy val false_pos_tile = FalsePosTile .mk

  lazy val correlation_tile = CorrelationTile .mk

  lazy val decision_tile = DecisionTile .mk (p3_acceptable_bias)

  def get_prediction (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    prediction_p_tile .apply (unzip_fst_tile .apply (message) )

  def get_result (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    result_p_tile .apply (unzip_snd_tile .apply (message) )

  def get_with_p (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Seq [Measure] ] =
    with_p_tile .apply (unzip_trd_tile .apply (message) )

  def get_false_pos (prediction : TileMessage [Seq [Measure] ] ) (
      result : TileMessage [Seq [Measure] ] ) : TileMessage [Seq [Measure] ] =
    false_pos_tile .apply (zip_tile .apply (prediction) (result) )

  def get_correlation (false_pos : TileMessage [Seq [Measure] ] ) (
      with_p : TileMessage [Seq [Measure] ] ) : TileMessage [Measure] =
    correlation_tile .apply (zip_tile .apply (false_pos) (with_p) )

  def get_correlation_plumbing (message : TileMessage [Seq [TileTriple [Actor, Actor, Actor] ] ] )
      : TileMessage [Measure] =
    get_correlation (
      get_false_pos (get_prediction (message) ) (get_result (message) )
    ) (get_with_p (message) )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    decision_tile .apply (
      get_correlation_plumbing (
        all_actor_triple_tile .apply (message)
      )
    )

}

case class UnbiasednessTile_ (p0_evaluation : Resource => Measure, p1_result : Actor => Measure, p2_with_p : Actor => Measure, p3_acceptable_bias : Measure) extends UnbiasednessTile

object UnbiasednessTile {
  def mk (p0_evaluation : Resource => Measure) (p1_result : Actor => Measure) (p2_with_p : Actor => Measure) (p3_acceptable_bias : Measure) : UnbiasednessTile =
    UnbiasednessTile_ (p0_evaluation, p1_result, p2_with_p, p3_acceptable_bias)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait UnzipPairFstTile
{



  def unzip_fst_list [A , B ] (list : Seq [TilePair [A, B] ] ) : Seq [A] =
    list .map ( pair => pair .fst)

  def apply [A , B ] (message : TileMessage [Seq [TilePair [A, B] ] ] )
      : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_fst_list (message .contents)
    )

}

case class UnzipPairFstTile_ () extends UnzipPairFstTile

object UnzipPairFstTile {
  def mk : UnzipPairFstTile =
    UnzipPairFstTile_ ()
}

trait UnzipPairSndTile
{



  def unzip_snd_list [A , B ] (list : Seq [TilePair [A, B] ] ) : Seq [B] =
    list .map ( pair => pair .snd)

  def apply [A , B ] (message : TileMessage [Seq [TilePair [A, B] ] ] )
      : TileMessage [Seq [B] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_snd_list (message .contents)
    )

}

case class UnzipPairSndTile_ () extends UnzipPairSndTile

object UnzipPairSndTile {
  def mk : UnzipPairSndTile =
    UnzipPairSndTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait UnzipTripleFstTile
{



  def unzip_fst_list [A , B , C ] (
      list : Seq [TileTriple [A, B, C] ] ) : Seq [A] =
    list .map ( triple => triple .fst)

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [A] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_fst_list (message .contents)
    )

}

case class UnzipTripleFstTile_ () extends UnzipTripleFstTile

object UnzipTripleFstTile {
  def mk : UnzipTripleFstTile =
    UnzipTripleFstTile_ ()
}

trait UnzipTripleSndTile
{



  def unzip_snd_list [A , B , C ] (
      list : Seq [TileTriple [A, B, C] ] ) : Seq [B] =
    list .map ( triple => triple .snd)

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [B] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_snd_list (message .contents)
    )

}

case class UnzipTripleSndTile_ () extends UnzipTripleSndTile

object UnzipTripleSndTile {
  def mk : UnzipTripleSndTile =
    UnzipTripleSndTile_ ()
}

trait UnzipTripleTrdTile
{



  def unzip_trd_list [A , B , C ] (
      list : Seq [TileTriple [A, B, C] ] ) : Seq [C] =
    list .map ( triple => triple .trd)

  def apply [A , B , C ] (
      message : TileMessage [Seq [TileTriple [A, B, C] ] ] ) : TileMessage [Seq [C] ] =
    TileMessageBuilder .mk .build (message .context) (message .outcome) (
      unzip_trd_list (message .contents)
    )

}

case class UnzipTripleTrdTile_ () extends UnzipTripleTrdTile

object UnzipTripleTrdTile {
  def mk : UnzipTripleTrdTile =
    UnzipTripleTrdTile_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AttributePTile
*/

trait WithPTile
{

  def   p : Actor => Measure

  def apply (message : TileMessage [Seq [Actor] ] ) : TileMessage [Seq [Measure] ] =
    AttributePTile .mk (p) .apply (message)

}

case class WithPTile_ (p : Actor => Measure) extends WithPTile

object WithPTile {
  def mk (p : Actor => Measure) : WithPTile =
    WithPTile_ (p)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

trait ZipTile
{



  def zip_lists [A , B ] (list0 : Seq [A] ) (list1 : Seq [B] )
      : Seq [TilePair [A, B] ] =
    list0
      .zip (list1)
      .map ( pair => TilePair_ [A, B] (pair ._1 , pair._2) )

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
