package soda.tiles.fairness.example.factory

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
import   soda.tiles.fairness.tool.Pipeline
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair
import   soda.tiles.fairness.example.pipeline.jainsindex.JainsIndexPipeline
import   soda.tiles.fairness.example.pipeline.individualfairness.IndividualFairnessPipeline
import   soda.tiles.fairness.example.pipeline.groupfairness.GroupFairnessPipeline
import   soda.tiles.fairness.example.pipeline.scoring.UnbiasednessPipeline
import   soda.tiles.fairness.example.pipeline.complementginiindex.ComplementGiniIndexPipeline
import   soda.tiles.fairness.example.pipeline.equality.EqualityPipeline
import   soda.tiles.fairness.example.pipeline.equity.EquityPipeline
import   soda.tiles.fairness.example.pipeline.envyfreeness.EnvyFreenessPipeline
import   soda.tiles.fairness.example.pipeline.envyfreeness.Preference
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsNoSubsidyPipeline
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsPerChildPipeline
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsPerFamilyPipeline
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsSingleGuardianPipeline





trait ExampleInstance
{

  def   agents : Seq [Agent]
  def   resources : Seq [Resource]
  def   outcome : Outcome
  def   agent_children_map : Map [Agent, Measure]
  def   agent_adults_map : Map [Agent, Measure]
  def   agent_need_map : Map [Agent, Measure]
  def   agent_income_map : Map [Agent, Measure]
  def   agent_preference_map : Map [Agent, Seq [Resource] ]
  def   agent_p_attribute_set : Set [Agent]
  def   agent_q_attribute_set : Set [Agent]
  def   agent_result_map : Map [Agent, Resource]
  def   resource_utility_map : Map [Resource, Measure]
  def   resource_positive_value : Resource
  def   resource_default_value : Resource
  def   pipelines : Seq [String]

  lazy val default_measure : Measure = Some (-1)

  lazy val default_resource : Resource = resource_default_value

  def get_or_else [A , B ] (map : Map [A, B] ) (key : A) (default : B) : B =
    map .get (key) match  {
      case Some (value) => value
      case None => default
    }

  def agent_children (agent : Agent) : Measure =
    get_or_else [Agent, Measure] (agent_children_map) (agent) (default_measure)

  def agent_adults (agent : Agent) : Measure =
    get_or_else [Agent, Measure] (agent_adults_map) (agent) (default_measure)

  def agent_need (agent : Agent) : Measure =
    get_or_else [Agent, Measure] (agent_need_map) (agent) (default_measure)

  def agent_income (agent : Agent) : Measure =
    get_or_else [Agent, Measure] (agent_income_map) (agent) (default_measure)

  def agent_p_attribute (agent : Agent) : Boolean =
    agent_p_attribute_set .contains (agent)

  def agent_q_attribute (agent : Agent) : Boolean =
    agent_q_attribute_set .contains (agent)

  def agent_result (agent : Agent) : Resource =
    get_or_else [Agent, Resource] (agent_result_map) (agent) (default_resource)

  def resource_utility (resource : Resource) : Measure =
    get_or_else [Resource, Measure] (resource_utility_map) (resource) (default_measure)

  lazy val context = "FairnessScenario"

  lazy val initial_message : TileMessage [Boolean] =
    TileMessageBuilder .mk .build (context) (outcome) (true)

}

case class ExampleInstance_ (agents : Seq [Agent], resources : Seq [Resource], outcome : Outcome, agent_children_map : Map [Agent, Measure], agent_adults_map : Map [Agent, Measure], agent_need_map : Map [Agent, Measure], agent_income_map : Map [Agent, Measure], agent_preference_map : Map [Agent, Seq [Resource] ], agent_p_attribute_set : Set [Agent], agent_q_attribute_set : Set [Agent], agent_result_map : Map [Agent, Resource], resource_utility_map : Map [Resource, Measure], resource_positive_value : Resource, resource_default_value : Resource, pipelines : Seq [String]) extends ExampleInstance

object ExampleInstance {
  def mk (agents : Seq [Agent]) (resources : Seq [Resource]) (outcome : Outcome) (agent_children_map : Map [Agent, Measure]) (agent_adults_map : Map [Agent, Measure]) (agent_need_map : Map [Agent, Measure]) (agent_income_map : Map [Agent, Measure]) (agent_preference_map : Map [Agent, Seq [Resource] ]) (agent_p_attribute_set : Set [Agent]) (agent_q_attribute_set : Set [Agent]) (agent_result_map : Map [Agent, Resource]) (resource_utility_map : Map [Resource, Measure]) (resource_positive_value : Resource) (resource_default_value : Resource) (pipelines : Seq [String]) : ExampleInstance =
    ExampleInstance_ (agents, resources, outcome, agent_children_map, agent_adults_map, agent_need_map, agent_income_map, agent_preference_map, agent_p_attribute_set, agent_q_attribute_set, agent_result_map, resource_utility_map, resource_positive_value, resource_default_value, pipelines)
}


trait ExampleInstanceBuilder
{



  import   soda.tiles.fairness.example.parser.YamlParser
  import   java.io.BufferedReader
  import   java.io.Reader

  lazy val separator = ","

  lazy val agents_key = "agents"

  lazy val resources_key = "resources"

  lazy val outcome_key = "outcome"

  lazy val agent_children_key = "agent_children"

  lazy val agent_adults_key = "agent_adults"

  lazy val agent_need_key = "agent_need"

  lazy val agent_income_key = "agent_income"

  lazy val agent_preference_key = "agent_preference"

  lazy val agent_p_attribute_key  = "agent_p_attribute"

  lazy val agent_q_attribute_key = "agent_q_attribute"

  lazy val agent_result_key = "agent_result"

  lazy val resource_utility_key = "resource_utility"

  lazy val resource_positive_value_key = "resource_positive_value"

  lazy val resource_default_value_key = "resource_default_value"

  lazy val pipelines_key = "pipelines"

  lazy val default_resource = "DEFAULT_RESOURCE"

  def to_measure (s : String) : Measure =
    s .toIntOption

  def to_seq_resource (s : String) : Seq [Resource] =
    s .split (separator)
      .map ( resource => resource .trim)
      .toSeq

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

  private def _get_agent_need_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Measure] =
    m .getOrElse (agent_need_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_agent_income_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Measure] =
    m .getOrElse (agent_income_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_agent_preference_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Seq [Resource] ] =
    m .getOrElse (agent_preference_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_seq_resource (pair ._2) ) )
      .toMap

  private def _get_agent_p_attribute_set (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Set [Agent] =
    m .getOrElse (agent_p_attribute_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSet

  private def _get_agent_q_attribute_set (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Set [Agent] =
    m .getOrElse (agent_q_attribute_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSet

  private def _get_agent_result_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Agent, Resource] =
    m .getOrElse (agent_result_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , pair ._2) )
      .toMap

  private def _get_resource_utility_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Map [Resource, Measure] =
    m .getOrElse (resource_utility_key , None)
      .iterator
      .map ( pair => Tuple2 (pair ._1 , to_measure (pair ._2) ) )
      .toMap

  private def _get_resource_positive_value (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Resource =
    m .getOrElse (resource_positive_value_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSeq
      .headOption
      .getOrElse (default_resource)

  private def _get_resource_default_value (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Resource =
    m .getOrElse (resource_default_value_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSeq
      .headOption
      .getOrElse (default_resource)

  private def _get_pipelines (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Seq [String] =
    m .getOrElse (pipelines_key , None)
      .iterator
      .map ( pair => pair ._1)
      .toSeq

  private def _build_from_map (m : Map [String, Seq [Tuple2 [String, String] ] ] )
      : Option [ExampleInstance] =
    Some (
      ExampleInstance .mk (
        _get_agents (m) ) (
        _get_resources (m) ) (
        _get_outcome (m) ) (
        _get_agent_children_map (m) ) (
        _get_agent_adults_map (m) ) (
        _get_agent_need_map (m) ) (
        _get_agent_income_map (m) ) (
        _get_agent_preference_map (m) ) (
        _get_agent_p_attribute_set (m) ) (
        _get_agent_q_attribute_set (m) ) (
        _get_agent_result_map (m) ) (
        _get_resource_utility_map (m) ) (
        _get_resource_positive_value (m) ) (
        _get_resource_default_value (m) ) (
        _get_pipelines (m)
      )
    )

  def build (s : Seq [Seq [Tuple2 [String, Seq [Tuple2 [String, String] ] ] ] ] )
      : Option [ExampleInstance] =
    s match  {
      case elem +: list => _build_from_map (elem .toMap)
      case otherwise => None
    }

  def from_yaml (reader : Reader) : Option [ExampleInstance] =
     build (YamlParser .mk .parse (reader) )

}

case class ExampleInstanceBuilder_ () extends ExampleInstanceBuilder

object ExampleInstanceBuilder {
  def mk : ExampleInstanceBuilder =
    ExampleInstanceBuilder_ ()
}


trait ExamplePipelineFactory
{



  def get_pipeline (name : String) (m : ExampleInstance) : Option [Pipeline] =
    if ( name == "EqualityPipeline"
    ) Some (
      EqualityPipeline .mk (m .resource_utility) )
    else if ( name == "EquityPipeline"
    ) Some (
      EquityPipeline .mk (m .agent_need) (m .resource_utility) )
    else if ( name == "EnvyFreenessPipeline"
    ) Some (
      EnvyFreenessPipeline .mk (Preference .mk (m .agent_preference_map) ) )
    else if ( name == "GroupFairnessPipeline"
    ) Some (
      GroupFairnessPipeline .mk (m .agent_p_attribute) )
    else if ( name == "IndividualFairnessPipeline"
    ) Some (
      IndividualFairnessPipeline .mk (m .agent_q_attribute) )
    else if ( name == "JainsIndexPipeline"
    ) Some (
      JainsIndexPipeline .mk (m .resource_utility) )
    else if ( name == "ComplementGiniIndexPipeline"
    ) Some (
      ComplementGiniIndexPipeline .mk (m .resource_utility) )
    else if ( name == "UnbiasednessPipeline"
    ) Some (
      UnbiasednessPipeline .mk (m .resource_positive_value) (m .agent_result) (m .agent_p_attribute) )
    else if ( name == "CcsNoSubsidyPipeline"
    ) Some (
      CcsNoSubsidyPipeline .mk (m .resource_utility) )
    else if ( name == "CcsPerChildPipeline"
    ) Some (
      CcsPerChildPipeline .mk (m .agent_children) (m .resource_utility) )
    else if ( name == "CcsPerFamilyPipeline"
    ) Some (
      CcsPerFamilyPipeline .mk (m .resource_utility) )
    else if ( name == "CcsSingleGuardianPipeline"
    ) Some (
      CcsSingleGuardianPipeline .mk (m .agent_adults) (m .resource_utility) )
    else None

}

case class ExamplePipelineFactory_ () extends ExamplePipelineFactory

object ExamplePipelineFactory {
  def mk : ExamplePipelineFactory =
    ExamplePipelineFactory_ ()
}



