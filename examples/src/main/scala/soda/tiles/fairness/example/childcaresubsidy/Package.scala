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
import   soda.tiles.fairness.tile.AtomicZipTile
import   soda.tiles.fairness.tile.AttributePTile
import   soda.tiles.fairness.tile.CombineBooleanTile
import   soda.tiles.fairness.tile.CorrelationTile
import   soda.tiles.fairness.tile.DecisionTile
import   soda.tiles.fairness.tile.FalsePosTile
import   soda.tiles.fairness.tile.FilterActorTile
import   soda.tiles.fairness.tile.MapPTile
import   soda.tiles.fairness.tile.NeededPTile
import   soda.tiles.fairness.tile.PredictionPTile
import   soda.tiles.fairness.tile.ReceivedSigmaPTile
import   soda.tiles.fairness.tile.ResultPTile
import   soda.tiles.fairness.tile.SigmaTile
import   soda.tiles.fairness.tile.UnzipPairFstTile
import   soda.tiles.fairness.tile.UnzipPairSndTile
import   soda.tiles.fairness.tile.UnzipTripleFstTile
import   soda.tiles.fairness.tile.UnzipTripleSndTile
import   soda.tiles.fairness.tile.UnzipTripleTrdTile
import   soda.tiles.fairness.tile.WithPTile
import   soda.tiles.fairness.tile.ZipTile
import   soda.tiles.fairness.pipeline.EqualityPipeline
import   soda.tiles.fairness.pipeline.EquityPipeline
import   soda.tiles.fairness.pipeline.UnbiasednessPipeline





trait CcsNoSubsidyPipeline
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

}

case class CcsNoSubsidyPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure) extends CcsNoSubsidyPipeline

object CcsNoSubsidyPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) : CcsNoSubsidyPipeline =
    CcsNoSubsidyPipeline_ (sigma, p_utility)
}


trait CcsPerFamilyPipeline
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

case class CcsPerFamilyPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure) extends CcsPerFamilyPipeline

object CcsPerFamilyPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) : CcsPerFamilyPipeline =
    CcsPerFamilyPipeline_ (sigma, p_utility)
}


trait CcsSingleGuardianPipeline
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

  lazy val unzip_fst_tile = UnzipPairFstTile .mk

  lazy val unzip_snd_tile = UnzipPairSndTile .mk

  def and_combination (b0 : Boolean) (b1 : Boolean) : Boolean =
    b0 && b1

  lazy val and_tile = CombineBooleanTile .mk (and_combination)

  lazy val atomic_zip_tile = AtomicZipTile .mk

  def condition_0 (a : Actor) : Boolean =
    adults (a) .getOrElse (0) == 1

  def condition_1 (a : Actor) : Boolean =
    adults (a) .getOrElse (0) > 1

  lazy val filter_actor_tile_0 = FilterActorTile .mk (condition_0)

  lazy val filter_actor_tile_1 = FilterActorTile .mk (condition_1)

  def get_branch_0 (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Boolean] =
    all_equal_tile .apply (
      received_sigma_p_tile .apply (
        filter_actor_tile_0 .apply (
          unzip_fst_tile .apply (message)
        )
      )
    )

  def get_branch_1 (message : TileMessage [Seq [TilePair [Actor, Actor] ] ] )
      : TileMessage [Boolean] =
    all_satisfy_p_tile .apply (
      received_sigma_p_tile .apply (
        filter_actor_tile_1 .apply (
          unzip_snd_tile .apply (message)
        )
      )
    )

  def apply (message : TileMessage [Boolean] ) : TileMessage [Boolean] =
    and_tile .apply (
      atomic_zip_tile .apply (
        get_branch_0 (all_actor_pair_tile .apply (message) )
      ) (
        get_branch_1 (all_actor_pair_tile .apply (message) )
      )
    )

}

case class CcsSingleGuardianPipeline_ (sigma : Measure => Measure => Measure, p_utility : Resource => Measure, adults : Actor => Measure) extends CcsSingleGuardianPipeline

object CcsSingleGuardianPipeline {
  def mk (sigma : Measure => Measure => Measure) (p_utility : Resource => Measure) (adults : Actor => Measure) : CcsSingleGuardianPipeline =
    CcsSingleGuardianPipeline_ (sigma, p_utility, adults)
}

