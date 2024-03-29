package soda.tiles.fairness.tool

/*
 * This package contains classes to model a fairness scenario.
 */





/*
directive lean
import Soda.tiles.fairness.tool.StringComparator
*/

type Identifier = String

type Actor = Identifier

type Resource = Identifier

type Context = Identifier

type Measure = Option [Int]

trait Assignment
{

  def   actor : Actor
  def   resource : Resource

}

case class Assignment_ (actor : Actor, resource : Resource) extends Assignment

object Assignment {
  def mk (actor : Actor) (resource : Resource) : Assignment =
    Assignment_ (actor, resource)
}

trait Outcome
{

  def   assignments : Seq [Assignment]

}

case class Outcome_ (assignments : Seq [Assignment]) extends Outcome

object Outcome {
  def mk (assignments : Seq [Assignment]) : Outcome =
    Outcome_ (assignments)
}

/**
 * This class contains functions to compare different types of objects.
 */

trait Comparator
{



/*
  directive lean
  def compareString (string0 : String) (string1 : String) : Int :=
    StringComparator.compare (string0) (string1)
*/

  def compareString (string0 : String) (string1 : String) : Int =
    StringComparator.mk.compare (string0) (string1)

  def compareIdentifier (identifier0 : Identifier) (identifier1 : Identifier) : Int =
    compareString (identifier0) (identifier1)

  def compareActor (actor0 : Actor) (actor1 : Actor) : Int =
    compareIdentifier (actor0) (actor1)

  def compareResource (resource0 : Resource) (resource1 : Resource) : Int =
    compareIdentifier (resource0) (resource1)

  def compareContext (context0 : Context) (context1 : Context) : Int =
    compareIdentifier (context0) (context1)

  private def _compareNone (measure : Measure) : Int =
    measure match  {
      case Some (value) => -1
      case None => 0
    }

  private def _compareSome (value : Int) (measure : Measure) : Int =
    measure match  {
      case Some (other_value) => value - other_value
      case None => 1
    }

  def compareMeasure (measure0 : Measure) (measure1 : Measure) : Int =
    measure0 match  {
      case Some (value) => _compareSome (value) (measure1)
      case None => _compareNone (measure1)
    }

  def compareAssignment (assignment0 : Assignment) (assignment1 : Assignment) : Int =
    if ( assignment0 .actor == assignment1 .actor
    ) compareResource (assignment0 .resource) (assignment1 .resource)
    else compareActor (assignment0 .actor) (assignment1 .actor)

}

case class Comparator_ () extends Comparator

object Comparator {
  def mk : Comparator =
    Comparator_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
*/

/**
 * This class contains helper functions for mathematical calculations.
 */

trait MathTool
{



  private def _tailrec_foldl [A , B ] (sequence : Seq [A] ) (current : B)
      (next : B => A => B) : B =
    sequence match  {
      case Nil => current
      case (head) +: (tail) =>
        _tailrec_foldl [A, B] (tail) (next (current) (head) ) (next)
    }

  def squared (x : Float) : Float =
    x * x

  private lazy val _sum_init : Float = 0

  private def _sum_next (accum : Float) (elem : Float) : Float =
    accum + elem

  def sum (seq : Seq [Float] ) : Float =
    _tailrec_foldl [Float, Float] (seq) (_sum_init) (_sum_next)

  def average (seq : Seq [Float] ) : Float =
    sum (seq) / seq .length .toFloat

}

case class MathTool_ () extends MathTool

object MathTool {
  def mk : MathTool =
    MathTool_ ()
}

/**
 * The main function in this class computes the Pearson correlation coefficient.
 *
 *  r_{x,y} =\frac{\sum _{i=1}^{n}(x_{i} - \bar{x})(y_{i} -
 *  \bar{y})}{\sqrt{\sum _{i=1}^{n}(x_{i} - \bar{x})^2} \sqrt{\sum ^{n} _{i=1}(y_{i} -
 *  \bar{y})^{2}}}
 */

trait Pearson
{

  def   xlist : Seq [Float]
  def   ylist : Seq [Float]

  private lazy val _mm : MathTool = MathTool .mk

/*
  directive lean
  notation "Math.sqrt" => Float.sqrt
  notation "_mm.average" => MathTool.average
  notation "_mm.sum" => MathTool.sum
  notation "_mm.squared" => MathTool.squared
*/

  def _to_Float (d : Double) : Float = d .toFloat

/*
  directive lean
  def _to_Float (x : Float) : Float := x
*/

  private def _sum_squared_diff_with (seq : Seq [Float] ) (x_average : Float) : Float =
    _mm .sum (seq .map ( x_i => _mm .squared (x_i - x_average) ) )

  def sum_squared_diff (seq : Seq [Float] ) : Float =
    _sum_squared_diff_with (seq) (_mm .average (seq) )

  private def _sqrt_sum_squared_diff (seq : Seq [Float] ) : Float =
    _to_Float ( Math.sqrt (sum_squared_diff (seq) ) )

  private lazy val _denominator : Float =
    _sqrt_sum_squared_diff (xlist) * _sqrt_sum_squared_diff (ylist)

  private def _multip (x_i : Float) (y_i : Float) (x_average : Float) (y_average : Float) : Float =
    (x_i - x_average) * (y_i - y_average)

  private def _numerator_with (pair_list : Seq [Tuple2 [Float, Float] ] ) (x_average : Float)
      (y_average : Float) : Float =
    _mm .sum (pair_list .map ( pair =>
      _multip (pair ._1) (pair ._2) (x_average) (y_average) ) )

  private lazy val _x_y_together : Seq [Tuple2 [Float, Float] ] =
    xlist .zip (ylist)

  private lazy val _numerator : Float =
    _numerator_with (_x_y_together) (_mm .average (xlist) ) (_mm .average (ylist) )

  lazy val coefficient : Float =
    _numerator / _denominator

}

case class Pearson_ (xlist : Seq [Float], ylist : Seq [Float]) extends Pearson

object Pearson {
  def mk (xlist : Seq [Float]) (ylist : Seq [Float]) : Pearson =
    Pearson_ (xlist, ylist)
}

trait ScoringCategory
{



  lazy val undefined_correlation : Int = 0

  lazy val no_correlation : Int = 1

  lazy val weak_positive_correlation : Int = 2

  lazy val weak_negative_correlation : Int = 3

  lazy val moderate_positive_correlation : Int = 4

  lazy val moderate_negative_correlation : Int = 5

  lazy val strong_positive_correlation : Int = 6

  lazy val strong_negative_correlation : Int = 7

  def categorize (x : Float) : Int =
    if ( (x > 0.5) && (x <= 1.0) ) strong_positive_correlation
    else if ( (x > 0.3) && (x <= 0.5) ) moderate_positive_correlation
    else if ( (x > 0) && (x <= 0.3) ) weak_positive_correlation
    else if ( (x == 0) ) no_correlation
    else if ( (x < 0) && (x >= -0.3) ) weak_negative_correlation
    else if ( (x < -0.3) && (x >= -0.5) ) moderate_negative_correlation
    else if ( (x < -0.5) && (x >= -1.0) ) strong_negative_correlation
    else undefined_correlation

}

case class ScoringCategory_ () extends ScoringCategory

object ScoringCategory {
  def mk : ScoringCategory =
    ScoringCategory_ ()
}




trait HelperTuple
{

  def   comparison : Int
  def   remaining : Seq [Char]

}

case class HelperTuple_ (comparison : Int, remaining : Seq [Char]) extends HelperTuple

object HelperTuple {
  def mk (comparison : Int) (remaining : Seq [Char]) : HelperTuple =
    HelperTuple_ (comparison, remaining)
}

/**
 * The main purpose of this class is to compare two strings. It behaves similarly to the
 * function `compareTo` provided by Scala.
 */

trait StringComparator
{



  lazy val it_is_greater : Int = 1

  lazy val it_is_less : Int = -1

  lazy val they_are_equal : Int = 0

  private def _tailrec_foldl_while [A , B ] (sequence : Seq [A] ) (current : B)
      (next : B => A => B) (condition : B => A => Boolean) : B =
    sequence match  {
      case Nil => current
      case (head) +: (tail) =>
        if ( (! (condition (current) (head) ) )
        ) current
        else _tailrec_foldl_while [A, B] (tail) (next (current) (head) ) (next) (condition)
    }

  private def _compare_char_seq (current_char : Char) (other_seq : Seq [Char] ) : HelperTuple =
    other_seq match  {
      case head +: tail =>
        if ( current_char < head
        ) HelperTuple .mk (it_is_less) (Nil)
        else
          if ( current_char > head
          ) HelperTuple .mk (it_is_greater) (Nil)
          else HelperTuple .mk (they_are_equal) (tail)
      case Nil => HelperTuple .mk (it_is_greater) (Nil)
    }

  private def _condition (other_seq_cmp : HelperTuple) (current_char : Char) : Boolean =
    other_seq_cmp .comparison == they_are_equal

  private def _next (other_seq_cmp : HelperTuple) (current_char : Char) : HelperTuple =
    if ( other_seq_cmp .comparison == they_are_equal
    ) _compare_char_seq (current_char) (other_seq_cmp .remaining)
    else other_seq_cmp

  private def _interpret_comparison (result : HelperTuple) : Int =
    result .remaining match  {
      case head +: tail => it_is_less
      case Nil => result .comparison
    }

  private def _compare_seq (seq0 : Seq [Char] ) (seq1 : Seq [Char] ) : Int =
    _interpret_comparison (
      _tailrec_foldl_while [Char, HelperTuple] (seq0) (
        HelperTuple .mk (they_are_equal) (seq1) ) (_next) (_condition)
    )

  def compare (str0 : String) (str1 : String) : Int =
    _compare_seq (str0 .toList) (str1 .toList)

}

case class StringComparator_ () extends StringComparator

object StringComparator {
  def mk : StringComparator =
    StringComparator_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.Entity
*/

trait TilePair [A , B ]
{

  def   fst : A
  def   snd : B

}

case class TilePair_ [A, B] (fst : A, snd : B) extends TilePair [A, B]

object TilePair {
  def mk [A, B] (fst : A) (snd : B) : TilePair [A, B] =
    TilePair_ [A, B] (fst, snd)
}

trait TileTriple [A , B , C ]
{

  def   fst : A
  def   snd : B
  def   trd : C

}

case class TileTriple_ [A, B, C] (fst : A, snd : B, trd : C) extends TileTriple [A, B, C]

object TileTriple {
  def mk [A, B, C] (fst : A) (snd : B) (trd : C) : TileTriple [A, B, C] =
    TileTriple_ [A, B, C] (fst, snd, trd)
}

trait TileMessage [A ]
{

  def   context : Context
  def   outcome : Outcome
  def   contents : A

}

case class TileMessage_ [A] (context : Context, outcome : Outcome, contents : A) extends TileMessage [A]

object TileMessage {
  def mk [A] (context : Context) (outcome : Outcome) (contents : A) : TileMessage [A] =
    TileMessage_ [A] (context, outcome, contents)
}

trait TileMessageBuilder
{



  def build [A ] (context : Context) (outcome : Outcome) (contents : A) : TileMessage [A] =
    TileMessage .mk (context) (outcome) (contents)

}

case class TileMessageBuilder_ () extends TileMessageBuilder

object TileMessageBuilder {
  def mk : TileMessageBuilder =
    TileMessageBuilder_ ()
}

