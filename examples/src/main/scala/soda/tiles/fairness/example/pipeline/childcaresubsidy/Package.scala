package soda.tiles.fairness.example.pipeline.childcaresubsidy

import   soda.tiles.fairness.tile.composite.AccumulatesTile
import   soda.tiles.fairness.tile.composite.AllAgentPairTile
import   soda.tiles.fairness.tile.composite.AllEqualTile
import   soda.tiles.fairness.tile.composite.ForallTile
import   soda.tiles.fairness.tile.composite.ZipSigmaTile
import   soda.tiles.fairness.tile.constant.AllAgentTile
import   soda.tiles.fairness.tile.derived.apply.CombineBooleanTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionPairFstTile
import   soda.tiles.fairness.tile.derived.apply.ProjectionPairSndTile
import   soda.tiles.fairness.tile.primitive.FilterTile
import   soda.tiles.fairness.tile.primitive.MapTile
import   soda.tiles.fairness.tile.primitive.TuplingPairTile
import   soda.tiles.fairness.tool.Agent
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Measure
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair





trait CcsInstance
{

  def   agents : Seq [Agent]
  def   resources : Seq [Resource]
  def   outcome : Outcome
  def   agent_children_map : Map [Agent, Measure]
  def   agent_adults_map : Map [Agent, Measure]
  def   agent_income_map : Map [Agent, Measure]
  def   resource_value_map : Map [Resource, Measure]
  def   pipelines : Seq [String]

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

case class CcsInstance_ (agents : Seq [Agent], resources : Seq [Resource], outcome : Outcome, agent_children_map : Map [Agent, Measure], agent_adults_map : Map [Agent, Measure], agent_income_map : Map [Agent, Measure], resource_value_map : Map [Resource, Measure], pipelines : Seq [String]) extends CcsInstance

object CcsInstance {
  def mk (agents : Seq [Agent]) (resources : Seq [Resource]) (outcome : Outcome) (agent_children_map : Map [Agent, Measure]) (agent_adults_map : Map [Agent, Measure]) (agent_income_map : Map [Agent, Measure]) (resource_value_map : Map [Resource, Measure]) (pipelines : Seq [String]) : CcsInstance =
    CcsInstance_ (agents, resources, outcome, agent_children_map, agent_adults_map, agent_income_map, resource_value_map, pipelines)
}


trait CcsInstanceBuilder
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
      : Option [CcsInstance] =
    Some (
      CcsInstance .mk (
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
      : Option [CcsInstance] =
    s match  {
      case a +: __soda__as => _build_from_map (a .toMap)
      case otherwise => None
    }

  def from_yaml (reader : Reader) : Option [CcsInstance] =
     build (YamlParser .mk .parse (reader) )

}

case class CcsInstanceBuilder_ () extends CcsInstanceBuilder

object CcsInstanceBuilder {
  def mk : CcsInstanceBuilder =
    CcsInstanceBuilder_ ()
}


trait CcsNoSubsidyPipeline
  extends
    CcsPipeline
{

  def   utility : Resource => Measure

  def is_equals_0 (measure : Measure) : Boolean =
    measure match  {
      case Some (0) => true
      case otherwise => false
    }

  lazy val forall_tile = ForallTile .mk [Measure] (is_equals_0)

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    forall_tile .apply (
      accumulates_tile .apply (
        all_agent_tile .apply (message)
      )
    )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsNoSubsidyPipeline_ (utility : Resource => Measure) extends CcsNoSubsidyPipeline

object CcsNoSubsidyPipeline {
  def mk (utility : Resource => Measure) : CcsNoSubsidyPipeline =
    CcsNoSubsidyPipeline_ (utility)
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Soda.tiles.fairness.tile.AtLeastTile
import Soda.tiles.fairness.tile.NeededPTile
import Soda.tiles.fairness.tile.ReceivedSigmaPTile
import Soda.tiles.fairness.tile.UnzipPairFstTile
import Soda.tiles.fairness.tile.UnzipPairSndTile
*/

/**
 * This pipeline returns 'true' when all the agents in the input receive a resource that
 * satisfies their needs, and 'false' otherwise.
 */

trait CcsPerChildPipeline
  extends
    CcsPipeline
{

  def   children : Agent => Measure
  def   utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val children_tile = MapTile .mk [Agent, Measure] (children)

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

  lazy val division_tile = ZipSigmaTile .mk (division)

  lazy val pair_fst_tile = ProjectionPairFstTile .mk [Seq [Agent] , Seq [Agent] ]

  lazy val pair_snd_tile = ProjectionPairSndTile .mk [Seq [Agent] , Seq [Agent] ]

  def apply_on_agents (pair : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      division_tile .apply (
        accumulates_tile .apply (pair_fst_tile .apply (pair) )
      ) (
        children_tile .apply (pair_snd_tile .apply (pair) )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    apply_on_agents (all_agent_pair_tile .apply (message) )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsPerChildPipeline_ (children : Agent => Measure, utility : Resource => Measure) extends CcsPerChildPipeline

object CcsPerChildPipeline {
  def mk (children : Agent => Measure) (utility : Resource => Measure) : CcsPerChildPipeline =
    CcsPerChildPipeline_ (children, utility)
}


trait CcsPerFamilyPipeline
  extends
    CcsPipeline
{

  def   utility : Resource => Measure

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val all_agent_tile = AllAgentTile .mk

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    all_equal_tile .apply (
      accumulates_tile .apply (
        all_agent_tile .apply (
          message
        )
      )
    )

  lazy val runner : TileMessage [Boolean] => TileMessage [Boolean] =
     message => apply (message)

}

case class CcsPerFamilyPipeline_ (utility : Resource => Measure) extends CcsPerFamilyPipeline

object CcsPerFamilyPipeline {
  def mk (utility : Resource => Measure) : CcsPerFamilyPipeline =
    CcsPerFamilyPipeline_ (utility)
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



  def get_pipeline (name : String) (m : CcsInstance) : Option [CcsPipeline] =
    if ( name == "CcsNoSubsidyPipeline"
    ) Some (
      CcsNoSubsidyPipeline .mk (m .resource_value) )
    else if ( name == "CcsPerChildPipeline"
    ) Some (
      CcsPerChildPipeline .mk (m .agent_children) (m .resource_value) )
    else if ( name == "CcsPerFamilyPipeline"
    ) Some (
      CcsPerFamilyPipeline .mk (m .resource_value) )
    else if ( name == "CcsSingleGuardianPipeline"
    ) Some (
      CcsSingleGuardianPipeline .mk (m .agent_adults) (m .resource_value) )
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

  def   utility : Resource => Measure
  def   adults : Agent => Measure

  def is_equals_0 (measure : Measure) : Boolean =
    measure match  {
      case Some (0) => true
      case otherwise => false
    }

  lazy val forall_tile = ForallTile .mk [Measure] (is_equals_0)

  lazy val all_equal_tile = AllEqualTile .mk

  lazy val accumulates_tile = AccumulatesTile .mk (utility)

  lazy val all_agent_tile = AllAgentTile .mk

  lazy val all_agent_pair_tile = AllAgentPairTile .mk

  lazy val pair_fst_tile = ProjectionPairFstTile .mk [Seq [Agent] , Seq [Agent] ]

  lazy val pair_snd_tile = ProjectionPairSndTile .mk [Seq [Agent] , Seq [Agent] ]

  def and_combination (b0 : Boolean) (b1 : Boolean) : Boolean =
    b0 && b1

  lazy val and_tile = CombineBooleanTile .mk (and_combination)

  lazy val tupling_tile = TuplingPairTile .mk [Boolean, Boolean]

  def condition_0 (a : Agent) : Boolean =
    adults (a) .getOrElse (0) == 1

  def condition_1 (a : Agent) : Boolean =
    adults (a) .getOrElse (0) > 1

  lazy val filter_agent_tile_0 = FilterTile .mk [Agent] (condition_0)

  lazy val filter_agent_tile_1 = FilterTile .mk [Agent] (condition_1)

  def get_branch_0 (message : TileMessage [Seq [Agent] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      accumulates_tile .apply (
        filter_agent_tile_0 .apply (
          message
        )
      )
    )

  def get_branch_1 (message : TileMessage [Seq [Agent] ] )
      : TileMessage [Boolean] =
    forall_tile .apply (
      accumulates_tile .apply (
        filter_agent_tile_1 .apply (
          message
        )
      )
    )

  def apply_on_agents (message : TileMessage [TilePair [Seq [Agent] , Seq [Agent] ] ] ) : TileMessage [Boolean] =
    and_tile .apply (
      tupling_tile .apply (
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

case class CcsSingleGuardianPipeline_ (utility : Resource => Measure, adults : Agent => Measure) extends CcsSingleGuardianPipeline

object CcsSingleGuardianPipeline {
  def mk (utility : Resource => Measure) (adults : Agent => Measure) : CcsSingleGuardianPipeline =
    CcsSingleGuardianPipeline_ (utility, adults)
}

