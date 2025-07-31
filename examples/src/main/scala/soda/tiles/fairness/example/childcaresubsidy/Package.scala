package soda.tiles.fairness.example.childcaresubsidy

import   soda.tiles.fairness.tool.Actor
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Context
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tile.AllActorPairTile
import   soda.tiles.fairness.tile.AllActorTile
import   soda.tiles.fairness.tile.AllActorTripleTile
import   soda.tiles.fairness.tile.AllAtLeastTile
import   soda.tiles.fairness.tile.AllEqual1Tile
import   soda.tiles.fairness.tile.AllEqualTile
import   soda.tiles.fairness.tile.AllSatisfyPTile
import   soda.tiles.fairness.tile.AttributePTile
import   soda.tiles.fairness.tile.CombineBooleanTile
import   soda.tiles.fairness.tile.CorrelationTile
import   soda.tiles.fairness.tile.DecisionTile
import   soda.tiles.fairness.tile.FalsePosTile
import   soda.tiles.fairness.tile.FilterActorTile
import   soda.tiles.fairness.tile.MapPTile
import   soda.tiles.fairness.tile.NeededPTile
import   soda.tiles.fairness.tile.PredictionPTile
import   soda.tiles.fairness.tile.ProjectionPairFstTile
import   soda.tiles.fairness.tile.ProjectionPairSndTile
import   soda.tiles.fairness.tile.ReceivedSigmaPTile
import   soda.tiles.fairness.tile.SigmaTile
import   soda.tiles.fairness.tile.TuplingPairTile
import   soda.tiles.fairness.tile.UnzipPairFstTile
import   soda.tiles.fairness.tile.UnzipPairSndTile
import   soda.tiles.fairness.tile.UnzipTripleFstTile
import   soda.tiles.fairness.tile.UnzipTripleSndTile
import   soda.tiles.fairness.tile.UnzipTripleTrdTile
import   soda.tiles.fairness.tile.ZipPairTile
import   soda.tiles.fairness.pipeline.EqualityPipeline
import   soda.tiles.fairness.pipeline.EquityPipeline
import   soda.tiles.fairness.pipeline.UnbiasednessPipeline





trait CcsAcromagatInstance
{

  def   actors : Seq [Actor]
  def   resources : Seq [Resource]
  def   outcome : Outcome
  def   actor_children_map : Map [Actor, Measure]
  def   actor_adults_map : Map [Actor, Measure]
  def   actor_income_map : Map [Actor, Measure]
  def   resource_value_map : Map [Resource, Measure]
  def   pipelines : Seq [String]

  private def _add_value_to (value : Int) (m : Measure) : Measure =
    m match  {
      case Some (other_value) => Some (value + other_value)
      case None => None
    }

  def measure_sum (a : Measure) (b : Measure) : Measure =
    a match  {
      case Some (value) => _add_value_to (value) (b)
      case None => None
    }

  def get_or_else [A ] (map : Map [A, Measure] ) (key : A) (default : Measure) : Measure =
    map .get (key) match  {
      case Some (value) => value
      case None => default
    }

  def actor_children (actor : Actor) : Measure =
    get_or_else [Actor] (actor_children_map) (actor) (Some (-1) )

  def actor_adults (actor : Actor) : Measure =
    get_or_else [Actor] (actor_adults_map) (actor) (Some (-1) )

  def actor_income (actor : Actor) : Measure =
    get_or_else [Actor] (actor_income_map) (actor) (Some (-1) )

  def resource_value (resource : Resource) : Measure =
    get_or_else [Resource] (resource_value_map) (resource) (Some (-1) )

  lazy val context = "ChildCareSubsidy"

  lazy val initial_message : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome) (true)

}

case class CcsAcromagatInstance_ (actors : Seq [Actor], resources : Seq [Resource], outcome : Outcome, actor_children_map : Map [Actor, Measure], actor_adults_map : Map [Actor, Measure], actor_income_map : Map [Actor, Measure], resource_value_map : Map [Resource, Measure], pipelines : Seq [String]) extends CcsAcromagatInstance

object CcsAcromagatInstance {
  def mk (actors : Seq [Actor]) (resources : Seq [Resource]) (outcome : Outcome) (actor_children_map : Map [Actor, Measure]) (actor_adults_map : Map [Actor, Measure]) (actor_income_map : Map [Actor, Measure]) (resource_value_map : Map [Resource, Measure]) (pipelines : Seq [String]) : CcsAcromagatInstance =
    CcsAcromagatInstance_ (actors, resources, outcome, actor_children_map, actor_adults_map, actor_income_map, resource_value_map, pipelines)
}


trait CcsAcromagatInstanceBuilder
{



  import   soda.tiles.fairness.example.parser.YamlParser
  import   java.io.BufferedReader
  import   java.io.Reader

  lazy val actors_key = "actors"

  lazy val resources_key = "resources"

  lazy val outcome_key = "outcome"

  lazy val actor_children_key = "actor_children"

  lazy val actor_adults_key = "actor_adults"

  lazy val actor_income_key = "actor_income"

  lazy val resource_value_key = "resource_value"

  lazy val pipelines_key = "pipelines"

  def to_measure (s : String) : Measure =
    s .toIntOption

  private def _get_actors (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Seq [Actor] =
    m .getOrElse (actors_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSeq

  private def _get_resources (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Seq [Resource] =
    m .getOrElse (resources_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSeq

  private def _get_outcome (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Outcome =
    Outcome .mk (
      m .getOrElse (outcome_key , None)
        .iterator
        .map( pair => Assignment .mk (pair._1) (pair ._2) )
        .toSeq
    )

  private def _get_actor_children_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Actor, Measure] =
    m .getOrElse (actor_children_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_actor_adults_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Actor, Measure] =
    m .getOrElse (actor_adults_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_actor_income_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Actor, Measure] =
    m .getOrElse (actor_income_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_resource_value_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Resource, Measure] =
    m .getOrElse (resource_value_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_pipelines (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Seq [String] =
    m .getOrElse (pipelines_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSeq

  private def _build_from_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Option [CcsAcromagatInstance] =
    Some (
      CcsAcromagatInstance .mk (
        _get_actors (m) ) (
        _get_resources (m) ) (
        _get_outcome (m) ) (
        _get_actor_children_map (m) ) (
        _get_actor_adults_map (m) ) (
        _get_actor_income_map (m) ) (
        _get_resource_value_map (m) ) (
        _get_pipelines (m)
      )
    )

  def build (s : Seq [Seq [Tuple2 [String, Seq [Tuple2 [String, String] ] ] ] ] )
      : Option [CcsAcromagatInstance] =
    s match  {
      case a +: __soda__as => _build_from_map (a .toMap)
      case otherwise => None
    }

  def from_yaml (reader : Reader) : Option [CcsAcromagatInstance] =
     build (YamlParser .mk .parse (reader) )

}

case class CcsAcromagatInstanceBuilder_ () extends CcsAcromagatInstanceBuilder

object CcsAcromagatInstanceBuilder {
  def mk : CcsAcromagatInstanceBuilder =
    CcsAcromagatInstanceBuilder_ ()
}


trait CcsNoSubsidyPipeline
  extends
    CcsPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p_utility : Resource => Measure

  def is_equals_0 (measure : Measure) : Boolean =
    measure match  {
      case Some (0) => true
      case otherwise => false
    }

  lazy val all_satisfy_p_tile = AllSatisfyPTile .mk (is_equals_0)

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p_utility)

  lazy val all_actor_tile = AllActorTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_satisfy_p_tile .apply (
      received_sigma_p_tile .apply (
        all_actor_tile .apply (message)
      )
    )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsNoSubsidyPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure) extends CcsNoSubsidyPipeline

object CcsNoSubsidyPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) : CcsNoSubsidyPipeline =
    CcsNoSubsidyPipeline_ (sigma, p_utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AtLeastTile
import Soda.tiles.fairness.tile.NeededPTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
import Soda.tiles.fairness.tile.UnzipPairFstTile
import Soda.tiles.fairness.tile.UnzipPairSndTile
import Soda.tiles.fairness.tile.ZipPairTile
*/

/**
 * This pipeline returns 'true' when all the actors in the input receive a resource that
 * satisfies their needs, and 'false' otherwise.
 */

trait CcsPerChildPipeline
  extends
    CcsPipeline
{

  def   sigma : Measure => Measure => Measure
  def   children : Actor => Measure
  def   utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (utility)

  lazy val children_tile = AttributePTile .mk (children)

  lazy val all_actor_pair_tile = AllActorPairTile .mk

  private def _division_with_2 (val0 : Int) (val1 : Int) : Measure =
    if ( val1 == 0
    ) None
    else Some (val0 / val1)

  private def _division_with (val0 : Int) (m1 : Measure) : Measure =
    m1 match  {
      case Some (val1) => _division_with_2 (val0) (val1)
      case None => None
    }

  def division (m0 : Measure) (m1 : Measure) : Measure =
    m0 match  {
      case Some (val0) => _division_with (val0) (m1)
      case None => None
    }

  lazy val division_tile = SigmaTile .mk (division)

  lazy val pair_fst_tile = ProjectionPairFstTile .mk

  lazy val pair_snd_tile = ProjectionPairSndTile .mk

  def apply_on_actors (pair : TileMessage [TilePair [Seq [Actor] , Seq [Actor] ] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      division_tile .apply (
        received_sigma_p_tile .apply (pair_fst_tile .apply (pair) )
      ) (
        children_tile .apply (pair_snd_tile .apply (pair) )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_actors (all_actor_pair_tile .apply (message) )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsPerChildPipeline_ (sigma : Measure => Measure => Measure, children : Actor => Measure, utility : Resource => Measure) extends CcsPerChildPipeline

object CcsPerChildPipeline {
  def mk (sigma : Measure => Measure => Measure) (children : Actor => Measure) (utility : Resource => Measure) : CcsPerChildPipeline =
    CcsPerChildPipeline_ (sigma, children, utility)
}


trait CcsPerFamilyPipeline
  extends
    CcsPipeline
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

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsPerFamilyPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure) extends CcsPerFamilyPipeline

object CcsPerFamilyPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) : CcsPerFamilyPipeline =
    CcsPerFamilyPipeline_ (sigma, p_utility)
}


trait CcsPipeline
{

  def   runner : TileMessage [Boolean] => TileMessage [Boolean]

  def run (initial : TileMessage [Boolean] ) : TileMessage [Boolean] =
    runner (initial)

}

case class CcsPipeline_ (runner : TileMessage [Boolean] => TileMessage [Boolean]) extends CcsPipeline

object CcsPipeline {
  def mk (runner : TileMessage [Boolean] => TileMessage [Boolean]) : CcsPipeline =
    CcsPipeline_ (runner)
}


trait CcsPipelineFactory
{



  def get_pipeline (name : String) (m : CcsAcromagatInstance) : Option [CcsPipeline] =
    if ( name == "CcsNoSubsidyPipeline"
    ) Some (
      CcsNoSubsidyPipeline .mk (m .measure_sum) (m .resource_value) )
    else if ( name == "CcsPerChildPipeline"
    ) Some (
      CcsPerChildPipeline .mk (m .measure_sum) (m .actor_children) (m .resource_value) )
    else if ( name == "CcsPerFamilyPipeline"
    ) Some (
      CcsPerFamilyPipeline .mk (m .measure_sum) (m .resource_value) )
    else if ( name == "CcsSingleGuardianPipeline"
    ) Some (
      CcsSingleGuardianPipeline .mk (m .measure_sum) (m .actor_adults) (m .resource_value) )
    else None

}

case class CcsPipelineFactory_ () extends CcsPipelineFactory

object CcsPipelineFactory {
  def mk : CcsPipelineFactory =
    CcsPipelineFactory_ ()
}




trait CcsSingleGuardianPipeline
  extends
    CcsPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p_utility : Resource => Measure
  def   adults : Actor => Measure

  def is_equals_0 (measure : Measure) : Boolean =
    measure match  {
      case Some (0) => true
      case otherwise => false
    }

  lazy val all_satisfy_p_tile = AllSatisfyPTile .mk (is_equals_0)

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p_utility)

  lazy val all_actor_tile = AllActorTile .mk

  lazy val all_actor_pair_tile = AllActorPairTile .mk

  lazy val pair_fst_tile = ProjectionPairFstTile .mk

  lazy val pair_snd_tile = ProjectionPairSndTile .mk

  def and_combination (b0 : Boolean) (b1 : Boolean) : Boolean =
    b0 && b1

  lazy val and_tile = CombineBooleanTile .mk (and_combination)

  lazy val pairing_tile = TuplingPairTile .mk

  def condition_0 (a : Actor) : Boolean =
    adults (a) .getOrElse (0) == 1

  def condition_1 (a : Actor) : Boolean =
    adults (a) .getOrElse (0) > 1

  lazy val filter_actor_tile_0 = FilterActorTile .mk (condition_0)

  lazy val filter_actor_tile_1 = FilterActorTile .mk (condition_1)

  def get_branch_0 (message : TileMessage [Seq [Actor] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        filter_actor_tile_0 .apply (
          message
        )
      )
    )

  def get_branch_1 (message : TileMessage [Seq [Actor] ] )
      : TileMessage [Boolean] =
    all_satisfy_p_tile .apply (
      received_sigma_p_tile .apply (
        filter_actor_tile_1 .apply (
          message
        )
      )
    )

  def apply_on_actors (message : TileMessage [TilePair [Seq [Actor] , Seq [Actor] ] ] ) : TileMessage [Boolean] =
    and_tile .apply (
      pairing_tile .apply (
        get_branch_0 (pair_fst_tile .apply (message) )
      ) (
        get_branch_1 (pair_snd_tile .apply (message) )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_actors (all_actor_pair_tile .apply (message) )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsSingleGuardianPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure, adults : Actor => Measure) extends CcsSingleGuardianPipeline

object CcsSingleGuardianPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) (adults : Actor => Measure) : CcsSingleGuardianPipeline =
    CcsSingleGuardianPipeline_ (sigma, p_utility, adults)
}

