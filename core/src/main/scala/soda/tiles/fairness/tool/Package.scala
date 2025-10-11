package soda.tiles.fairness.tool

/*
 * This package contains classes to model a fairness scenario.
 */





/*
directive lean
import Soda.tiles.fairness.tool.StringComparator
*/

type Identifier = String

type Agent = Identifier

type Resource = Identifier

type Context = Identifier

type Measure = Option [Int]

trait MeasureMod
{



  lazy val zero : Measure = Some (0)

  lazy val one : Measure = Some (1)

  def is_equals_0 (measure : Measure) : Boolean =
    measure match  {
      case Some (0) => true
      case otherwise => false
    }

  def add_value_to (value : Int) (m : Measure) : Measure =
    m match  {
      case Some (other_value) => Some (value + other_value)
      case None => None
    }

  def plus (a : Measure) (b : Measure) : Measure =
    a match  {
      case Some (value) => add_value_to (value) (b)
      case None => None
    }

  private def _divide_integers (a : Int) (b : Int) : Measure =
    if ( b == 0
    ) None
    else Some (a / b)

  def divide_by (a : Measure) (b : Int) : Measure =
    a match  {
      case Some (value) => _divide_integers (value) (b)
      case None => None
    }

  def divide (a : Measure) (b : Measure) : Measure =
    b match  {
      case Some (value) => divide_by (a) (value)
      case None => None
    }

}

case class MeasureMod_ () extends MeasureMod

object MeasureMod {
  def mk : MeasureMod =
    MeasureMod_ ()
}

trait Assignment
{

  def   agent : Agent
  def   resource : Resource

}

case class Assignment_ (agent : Agent, resource : Resource) extends Assignment

object Assignment {
  def mk (agent : Agent) (resource : Resource) : Assignment =
    Assignment_ (agent, resource)
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

trait OutcomeMod
{



  def get_assignments (outcome : Outcome) (a : Agent) : Seq [Assignment] =
    outcome
      .assignments
      .filter ( assignment => assignment .agent == a)

  def get_resources (outcome : Outcome) (a : Agent) : Seq [Resource] =
    get_assignments (outcome) (a)
      .map ( assignment => assignment .resource)

  def receives (outcome : Outcome) (a : Agent) (r : Resource) : Boolean =
    outcome
      .assignments
      .contains (Assignment .mk (a) (r) )

}

case class OutcomeMod_ () extends OutcomeMod

object OutcomeMod {
  def mk : OutcomeMod =
    OutcomeMod_ ()
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

  def compareAgent (agent0 : Agent) (agent1 : Agent) : Int =
    compareIdentifier (agent0) (agent1)

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
    if ( assignment0 .agent == assignment1 .agent
    ) compareResource (assignment0 .resource) (assignment1 .resource)
    else compareAgent (assignment0 .agent) (assignment1 .agent)

}

case class Comparator_ () extends Comparator

object Comparator {
  def mk : Comparator =
    Comparator_ ()
}


/*
directive lean
import Soda.tiles.fairness.tool.TileMessage
import Batteries.Lean.Float
*/

/*
directive lean
notation "Number" => Float
def as_number (x : Int) : Number := Int.divFloat (x) (1)
def as_pair (x : Prod (Number) (Number) ) : TilePair (Number) (Number) := TilePair.mk (x.fst) (x.snd)
*/

type Number = Double
def as_number (x : Int) : Number = x.toDouble
def as_pair (x : Tuple2 [Number, Number] ) : TilePair [Number, Number] = TilePair.mk [Number, Number] (x._1) (x._2)

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

  def squared (x : Number) : Number =
    x * x

  def sqrt (x : Number) : Number =
    Math .sqrt (x)

  private lazy val _sum_init : Number = 0

  private def _sum_next (accum : Number) (elem : Number) : Number =
    accum + elem

  def sum (seq : Seq [Number] ) : Number =
    _tailrec_foldl [Number, Number] (seq) (_sum_init) (_sum_next)

  def average (seq : Seq [Number] ) : Number =
    sum (seq) / as_number (seq .length)

  def abs (x : Number) : Number =
    if ( x < 0
    ) (-1) * x
    else x

  def is_within (maybeNumber : Option [Number] ) (center : Double) (epsilon : Double) : Boolean =
    maybeNumber
      .exists ( value =>
        abs (value - center) <= epsilon)

}

case class MathTool_ () extends MathTool

object MathTool {
  def mk : MathTool =
    MathTool_ ()
}

trait SeqPair
{

  def   xlist : Seq [Number]
  def   ylist : Seq [Number]

}

case class SeqPair_ (xlist : Seq [Number], ylist : Seq [Number]) extends SeqPair

object SeqPair {
  def mk (xlist : Seq [Number]) (ylist : Seq [Number]) : SeqPair =
    SeqPair_ (xlist, ylist)
}

/**
 * The main function in this class computes the Pearson correlation coefficient.
 * It uses a direct formula.
 *
 *  r_{x,y} = \frac{\sum _{i=1}^{n}(x_{i} - \bar{x})(y_{i} -
 *  \bar{y})}{\sqrt{\sum _{i=1}^{n}(x_{i} - \bar{x})^2} \sqrt{\sum ^{n} _{i=1}(y_{i} -
 *  \bar{y})^{2}}}
 */

trait PearsonCorrDirect
{



  private lazy val _mm : MathTool = MathTool .mk

/*
  directive lean
  notation "_mm.sqrt" => Float.sqrt
  notation "_mm.average" => MathTool.average
  notation "_mm.sum" => MathTool.sum
  notation "_mm.squared" => MathTool.squared
*/

  private def _sum_squared_diff_with (seq : Seq [Number] ) (x_average : Number) : Number =
    _mm .sum (seq .map ( x_i => _mm .squared (x_i - x_average) ) )

  def sum_squared_diff (seq : Seq [Number] ) : Number =
    _sum_squared_diff_with (seq) (_mm .average (seq) )

  private def _sqrt_sum_squared_diff (seq : Seq [Number] ) : Number =
    _mm .sqrt (sum_squared_diff (seq) )

  private def _denominator (m : SeqPair) : Number =
    _sqrt_sum_squared_diff (m .xlist) * _sqrt_sum_squared_diff (m .ylist)

  private def _multip (x_i : Number) (y_i : Number) (x_average : Number) (y_average : Number) : Number =
    (x_i - x_average) * (y_i - y_average)

  private def _numerator_with (pair_list : Seq [TilePair [Number, Number] ] ) (x_average : Number)
      (y_average : Number) : Number =
    _mm .sum (pair_list .map ( pair =>
      _multip (pair .fst) (pair .snd) (x_average) (y_average) ) )

  private def _x_y_together (m : SeqPair) : Seq [TilePair [Number, Number] ] =
    (m .xlist .zip (m .ylist) ) .map ( elem => as_pair (elem) )

  private def _numerator (m : SeqPair) : Number =
    _numerator_with (_x_y_together (m) ) (_mm .average (m .xlist) ) (_mm .average (m .ylist) )

  def coefficient_with (numerator : Number) (denominator : Number) : Option [Number] =
    if ( denominator == 0
    ) None
    else Some (numerator / denominator)

  def coefficient (m : SeqPair) : Option [Number] =
    coefficient_with (_numerator (m) ) (_denominator (m) )

}

case class PearsonCorrDirect_ () extends PearsonCorrDirect

object PearsonCorrDirect {
  def mk : PearsonCorrDirect =
    PearsonCorrDirect_ ()
}

/**
 * The main function in this class computes the Pearson correlation coefficient.
 * It computes the sample covariance and the sample standard deviation.
 *
 * r_{X,Y} = \frac{\mathrm{Cov}(X, Y)}{\sigma_{X} \, \sigma_{Y}}
 */

trait PearsonCorrCovariance
{



  private lazy val _mm : MathTool = MathTool .mk

  private def _covariance_with (xs : Seq [Number] ) (ys : Seq [Number] ) (mu_x : Number) (mu_y : Number) (n : Number)
      : Option [Number] =
    if ( n > 1
    ) Some (
      _mm .sum (
        xs
          .zip (ys)
          .map ( pair => (pair ._1 - mu_x) * (pair ._2 - mu_y) )
      ) / (n - 1)
    )
    else None

  /** Sample covariance: Cov(X,Y) = Σ (xᵢ - μₓ)(yᵢ - μᵧ) / (n - 1) */

  def covariance (xs : Seq [Number] ) (ys : Seq [Number] ) : Option [Number] =
    _covariance_with (xs) (ys) (_mm .average (xs) ) (_mm .average (ys) ) (as_number (xs .length) )

  /** Sample variance: Var(X) = Cov(X, X) */

  def variance (seq : Seq [Number] ) : Option [Number] =
    covariance (seq) (seq)

  /** Sample standard deviation: σₓ = sqrt(Var(X)) */

  def stddev (seq : Seq [Number] ) : Option [Number] =
    variance (seq)
      .map ( v => _mm .sqrt (v) )

  def coefficient_with (cov_xy : Option [Number] ) (stddev_x : Option [Number] ) (stddev_y : Option [Number] )
      : Option [Number] =
    stddev_x
      .filter ( x => ! (x == 0) )
      .flatMap ( stddev_x_val =>
        stddev_y
          .filter ( x => ! (x == 0) )
          .flatMap ( stddev_y_val =>
            cov_xy .map ( cov_xy_val =>
              cov_xy_val / (stddev_x_val * stddev_y_val)
            )
          )
      )

  /** Pearson correlation coefficient: r = Cov(X,Y) / (σₓ * σᵧ) */

  def coefficient (m : SeqPair) : Option [Number] =
    coefficient_with (
      covariance (m .xlist) (m .ylist)
    ) (
      stddev (m .xlist)
    ) (
      stddev (m .ylist)
    )

}

case class PearsonCorrCovariance_ () extends PearsonCorrCovariance

object PearsonCorrCovariance {
  def mk : PearsonCorrCovariance =
    PearsonCorrCovariance_ ()
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

  def categorize (x : Number) : Int =
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

