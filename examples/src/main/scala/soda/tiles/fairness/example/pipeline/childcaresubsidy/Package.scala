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
import   soda.tiles.fairness.tool.Number
import   soda.tiles.fairness.tool.Outcome
import   soda.tiles.fairness.tool.Pipeline
import   soda.tiles.fairness.tool.Resource
import   soda.tiles.fairness.tool.TileMessage
import   soda.tiles.fairness.tool.TileMessageBuilder
import   soda.tiles.fairness.tool.TilePair





trait CcsNoSubsidyPipeline
  extends
    Pipeline
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

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => as_number (apply (message) )

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
    Pipeline
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

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => as_number (apply (message) )

}

case class CcsPerChildPipeline_ (children : Agent => Measure, utility : Resource => Measure) extends CcsPerChildPipeline

object CcsPerChildPipeline {
  def mk (children : Agent => Measure) (utility : Resource => Measure) : CcsPerChildPipeline =
    CcsPerChildPipeline_ (children, utility)
}


trait CcsPerFamilyPipeline
  extends
    Pipeline
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

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => as_number (apply (message) )

}

case class CcsPerFamilyPipeline_ (utility : Resource => Measure) extends CcsPerFamilyPipeline

object CcsPerFamilyPipeline {
  def mk (utility : Resource => Measure) : CcsPerFamilyPipeline =
    CcsPerFamilyPipeline_ (utility)
}


trait CcsSingleGuardianPipeline
  extends
    Pipeline
{

  def   adults : Agent => Measure
  def   utility : Resource => Measure

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

  lazy val runner : TileMessage [Boolean] => TileMessage [Number] =
     message => as_number (apply (message) )

}

case class CcsSingleGuardianPipeline_ (adults : Agent => Measure, utility : Resource => Measure) extends CcsSingleGuardianPipeline

object CcsSingleGuardianPipeline {
  def mk (adults : Agent => Measure) (utility : Resource => Measure) : CcsSingleGuardianPipeline =
    CcsSingleGuardianPipeline_ (adults, utility)
}

