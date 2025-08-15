package soda.tiles.fairness.example.childcaresubsidy

import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Context
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.tile.constant.AllAgentPairTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.constant.AllAgentTripleTile
import   soda.tiles.fairness.tile.fold.AllAtLeastTile
import   soda.tiles.fairness.tile.fold.AllEqual1Tile
import   soda.tiles.fairness.tile.fold.AllEqualTile
import   soda.tiles.fairness.tile.map.AttributePTile
import   soda.tiles.fairness.tile.specific.CorrelationTile
import   soda.tiles.fairness.tile.apply.DecisionTile
import   soda.tiles.fairness.tile.specific.FalsePosTile
import   soda.tiles.fairness.tile.map.NeededPTile
import   soda.tiles.fairness.tile.fold.PredictionPTile
import   soda.tiles.fairness.tile.apply.ProjectionPairFstTile
import   soda.tiles.fairness.tile.apply.ProjectionPairSndTile
import   soda.tiles.fairness.tile.apply.ProjectionTripleFstTile
import   soda.tiles.fairness.tile.apply.ProjectionTripleSndTile
import   soda.tiles.fairness.tile.apply.ProjectionTripleTrdTile
import   soda.tiles.fairness.tile.fold.ReceivedSigmaPTile
import   soda.tiles.fairness.tile.fold.SigmaTile
import   soda.tiles.fairness.tile.map.UnzipPairFstTile
import   soda.tiles.fairness.tile.map.UnzipPairSndTile
import   soda.tiles.fairness.tile.map.UnzipTripleFstTile
import   soda.tiles.fairness.tile.map.UnzipTripleSndTile
import   soda.tiles.fairness.tile.map.UnzipTripleTrdTile
import   soda.tiles.fairness.tile.zip.ZipPairTile
import   soda.tiles.fairness.tile.zip.ZipTripleTile
import   soda.tiles.fairness.tile.apply.TuplingPairTile
import   soda.tiles.fairness.tile.apply.CombineBooleanTile
import   soda.tiles.fairness.tile.fold.AllSatisfyPTile
import   soda.tiles.fairness.tile.filter.FilterTile
import   soda.tiles.fairness.pipeline.EqualityPipeline
import   soda.tiles.fairness.pipeline.EquityPipeline
import   soda.tiles.fairness.pipeline.UnbiasednessPipeline





trait CcsAcromagatInstance
{

  def   agents : Seq [Agent]
  def   resources : Seq [Resource]
  def   outcome : Outcome
  def   agent_children_map : Map [Agent, Measure]
  def   agent_adults_map : Map [Agent, Measure]
  def   agent_income_map : Map [Agent, Measure]
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

  def agent_children (agent : Agent) : Measure =
    get_or_else [Agent] (agent_children_map) (agent) (Some (-1) )

  def agent_adults (agent : Agent) : Measure =
    get_or_else [Agent] (agent_adults_map) (agent) (Some (-1) )

  def agent_income (agent : Agent) : Measure =
    get_or_else [Agent] (agent_income_map) (agent) (Some (-1) )

  def resource_value (resource : Resource) : Measure =
    get_or_else [Resource] (resource_value_map) (resource) (Some (-1) )

  lazy val context = "ChildCareSubsidy"

  lazy val initial_message : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome) (true)

}

case class CcsAcromagatInstance_ (agents : Seq [Agent], resources : Seq [Resource], outcome : Outcome, agent_children_map : Map [Agent, Measure], agent_adults_map : Map [Agent, Measure], agent_income_map : Map [Agent, Measure], resource_value_map : Map [Resource, Measure], pipelines : Seq [String]) extends CcsAcromagatInstance

object CcsAcromagatInstance {
  def mk (agents : Seq [Agent]) (resources : Seq [Resource]) (outcome : Outcome) (agent_children_map : Map [Agent, Measure]) (agent_adults_map : Map [Agent, Measure]) (agent_income_map : Map [Agent, Measure]) (resource_value_map : Map [Resource, Measure]) (pipelines : Seq [String]) : CcsAcromagatInstance =
    CcsAcromagatInstance_ (agents, resources, outcome, agent_children_map, agent_adults_map, agent_income_map, resource_value_map, pipelines)
}


trait CcsAcromagatInstanceBuilder
{



  import   soda.tiles.fairness.example.parser.YamlParser
  import   java.io.BufferedReader
  import   java.io.Reader

  lazy val agents_key = "agents"

  lazy val resources_key = "resources"

  lazy val outcome_key = "outcome"

  lazy val agent_children_key = "agent_children"

  lazy val agent_adults_key = "agent_adults"

  lazy val agent_income_key = "agent_income"

  lazy val resource_value_key = "resource_value"

  lazy val pipelines_key = "pipelines"

  def to_measure (s : String) : Measure =
    s .toIntOption

  private def _get_agents (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Seq [Agent] =
    m .getOrElse (agents_key , None)
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

  private def _get_agent_children_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Measure] =
    m .getOrElse (agent_children_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_agent_adults_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Measure] =
    m .getOrElse (agent_adults_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_agent_income_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Measure] =
    m .getOrElse (agent_income_key , None)
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
        _get_agents (m) ) (
        _get_resources (m) ) (
        _get_outcome (m) ) (
        _get_agent_children_map (m) ) (
        _get_agent_adults_map (m) ) (
        _get_agent_income_map (m) ) (
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

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_satisfy_p_tile .apply (
      received_sigma_p_tile .apply (
        all_agent_tile .apply (message)
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
 * This pipeline returns 'true' when all the agents in the input receive a resource that
 * satisfies their needs, and 'false' otherwise.
 */

trait CcsPerChildPipeline
  extends
    CcsPipeline
{

  def   sigma : Measure => Measure => Measure
  def   children : Agent => Measure
  def   utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (utility)

  lazy val children_tile = AttributePTile .mk (children)

  lazy val all_agent_pair_tile = AllAgentPairTile .mk

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

  def apply_on_agents (pair : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      division_tile .apply (
        received_sigma_p_tile .apply (pair_fst_tile .apply (pair) )
      ) (
        children_tile .apply (pair_snd_tile .apply (pair) )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_agents (all_agent_pair_tile .apply (message) )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsPerChildPipeline_ (sigma : Measure => Measure => Measure, children : Agent => Measure, utility : Resource => Measure) extends CcsPerChildPipeline

object CcsPerChildPipeline {
  def mk (sigma : Measure => Measure => Measure) (children : Agent => Measure) (utility : Resource => Measure) : CcsPerChildPipeline =
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

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        all_agent_tile .apply (message)
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


trait CcsPipelineFagenty
{



  def get_pipeline (name : String) (m : CcsAcromagatInstance) : Option [CcsPipeline] =
    if ( name == "CcsNoSubsidyPipeline"
    ) Some (
      CcsNoSubsidyPipeline .mk (m .measure_sum) (m .resource_value) )
    else if ( name == "CcsPerChildPipeline"
    ) Some (
      CcsPerChildPipeline .mk (m .measure_sum) (m .agent_children) (m .resource_value) )
    else if ( name == "CcsPerFamilyPipeline"
    ) Some (
      CcsPerFamilyPipeline .mk (m .measure_sum) (m .resource_value) )
    else if ( name == "CcsSingleGuardianPipeline"
    ) Some (
      CcsSingleGuardianPipeline .mk (m .measure_sum) (m .agent_adults) (m .resource_value) )
    else None

}

case class CcsPipelineFagenty_ () extends CcsPipelineFagenty

object CcsPipelineFagenty {
  def mk : CcsPipelineFagenty =
    CcsPipelineFagenty_ ()
}




trait CcsSingleGuardianPipeline
  extends
    CcsPipeline
{

  def   sigma : Measure => Measure => Measure
  def   p_utility : Resource => Measure
  def   adults : Agent => Measure

  def is_equals_0 (measure : Measure) : Boolean =
    measure match  {
      case Some (0) => true
      case otherwise => false
    }

  lazy val all_satisfy_p_tile = AllSatisfyPTile .mk (is_equals_0)

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val received_sigma_p_tile = ReceivedSigmaPTile .mk (sigma) (p_utility)

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val all_agent_pair_tile = AllAgentPairTile .mk

  lazy val pair_fst_tile = ProjectionPairFstTile .mk

  lazy val pair_snd_tile = ProjectionPairSndTile .mk

  def and_combination (b0 : Boolean) (b1 : Boolean) : Boolean =
    b0 && b1

  lazy val and_tile = CombineBooleanTile .mk (and_combination)

  lazy val pairing_tile = TuplingPairTile .mk

  def condition_0 (a : Agent) : Boolean =
    adults (a) .getOrElse (0) == 1

  def condition_1 (a : Agent) : Boolean =
    adults (a) .getOrElse (0) > 1

  lazy val filter_agent_tile_0 = FilterTile .mk [Agent] (condition_0)

  lazy val filter_agent_tile_1 = FilterTile .mk [Agent] (condition_1)

  def get_branch_0 (message : TileMessage [Seq [Agent] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        filter_agent_tile_0 .apply (
          message
        )
      )
    )

  def get_branch_1 (message : TileMessage [Seq [Agent] ] )
      : TileMessage [Boolean] =
    all_satisfy_p_tile .apply (
      received_sigma_p_tile .apply (
        filter_agent_tile_1 .apply (
          message
        )
      )
    )

  def apply_on_agents (message : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] ) : TileMessage [Boolean] =
    and_tile .apply (
      pairing_tile .apply (
        get_branch_0 (pair_fst_tile .apply (message) )
      ) (
        get_branch_1 (pair_snd_tile .apply (message) )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_agents (all_agent_pair_tile .apply (message) )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsSingleGuardianPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure, adults : Agent => Measure) extends CcsSingleGuardianPipeline

object CcsSingleGuardianPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) (adults : Agent => Measure) : CcsSingleGuardianPipeline =
    CcsSingleGuardianPipeline_ (sigma, p_utility, adults)
}

