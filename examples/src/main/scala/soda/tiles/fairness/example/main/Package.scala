package soda.tiles.fairness.example.main

/*
 * This package is for the main class.
 * This is the entry point when the application is executed from a terminal.
 */

import   java.io.FileReader
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsInstance
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsInstanceBuilder
import   soda.tiles.fairness.example.pipeline.childcaresubsidy.CcsPipelineFactory





/**
 * This is the main entry point.
 */

trait Main
{



  lazy val help = "" +
    "\nTiles" +
    "\n" +
    "\nCopyright 2023--2025 Julian Alfredo Mendez" +
    "\n" +
    "\nhttps://github.com/julianmendez/tiles" +
    "\n" +
    "\nTiles is a framework to create formal configurations of constraints." +
    "\nThis is an example of a Child Care Subsidy scenario." +
    "\nThis application outputs the results to the standard output." +
    "\n" +
    "\nUsage:" +
    "\n" +
    "\n  tiles INPUT_FILE" +
    "\n" +
    "\nwhere INPUT_FILE is the name of a YAML file." +
    "\n" +
    "\nExceptions:" +
    "\n" +
    "\n  if the input file name is invalid, this throws an exception" +
    "\n" +
    "\n  if the input file is unreadable, this throws an exception" +
    "\n" +
    "\n  if the input file has an invalid format, this output an error as output" +
    "\n" +
    "\n  if the input file has some invalid value, this output an error as output" +
    "\n"

  lazy val error_invalid_pipeline = "" +
    "ERROR: Invalid pipeline name. " +
    "The factory of known pipelines does not contain the provided pipeline name"

  lazy val error_invalid_input_file = "" +
    "ERROR: Invalid input file. " +
    "The input file needs to be a YAML file with a specific structure. " +
    "Common errors include wrong indentation, an extra, missing or misplaced colon (':'), " +
    "and a misspelled attribute name."

  private def _execute_and_report_pipeline (name : String) (input : CcsInstance) : String =
    CcsPipelineFactory .mk
      .get_pipeline (name) (input)
      .map( pipeline =>
        name + ": " +
        pipeline .run(input .initial_message)
          .contents
          .toString
      )
      .getOrElse(error_invalid_pipeline + " '" + name + "'.")

  private def _execute_and_report_with (input : CcsInstance) : String =
    input
      .pipelines
      .map ( name =>
        _execute_and_report_pipeline (name) (input)
      )
      .mkString ("\n")

  def execute_and_report (input : Option [CcsInstance] ) : String =
    input match  {
      case Some (instance) => _execute_and_report_with (instance)
      case otherwise => error_invalid_input_file
    }

  def execute (arguments : Seq [String] ) : String =
    if ( arguments .length == 0
    ) help
    else
      execute_and_report (
        CcsInstanceBuilder .mk
          .from_yaml (FileReader (arguments .head) )
      )

  def main (arguments : Array [String] ) : Unit =
    println (execute (arguments .toSeq) )

}

object EntryPoint {
  def main (args: Array [String]): Unit = Main_ ().main (args)
}


case class Main_ () extends Main

object Main {
  def mk : Main =
    Main_ ()
}

