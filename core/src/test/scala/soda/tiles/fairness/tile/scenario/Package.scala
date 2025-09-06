package soda.tiles.fairness.tile.scenario

/*
 * This package contains scenarios for testing.
 */

import   org.scalatest.funsuite.AnyFunSuite
import   soda.tiles.fairness.tool.Assignment
import   soda.tiles.fairness.tool.Outcome

trait ScenarioExample
{



  lazy val context = "context"

  lazy val resource0 = "small box - 0.1 m"

  lazy val resource1 = "medium box - 0.2 m"

  lazy val resource2 = "large box - 0.3 m"

  lazy val agent0 = "Anna A"

  lazy val agent1 = "Bob B"

  lazy val agent2 = "Charlie C"

  lazy val outcome0 : Outcome =
    Outcome .mk (
      Seq [Assignment] (
        Assignment .mk (agent0) (resource2) ,
        Assignment .mk (agent1) (resource1) ,
        Assignment .mk (agent2) (resource0)
      )
    )

}

case class ScenarioExample_ () extends ScenarioExample

object ScenarioExample {
  def mk : ScenarioExample =
    ScenarioExample_ ()
}

