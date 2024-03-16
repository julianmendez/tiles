package soda.tiles.fairness.parser

import   org.scalatest.funsuite.AnyFunSuite
import   org.scalatest.Assertion
import   java.nio.file.Files
import   java.nio.file.Paths
import   java.io.StringReader





trait Example0Instance
{



  lazy val instance = Seq (
    Seq (
      Tuple2 ("actors" ,
        Seq (
          Tuple2 ("family A" , ".") ,
          Tuple2 ("family B" , ".") ,
          Tuple2 ("family C" , ".")
        )
      ) ,
      Tuple2 ("resources" ,
        Seq (
          Tuple2 ("no subsidy 0" , ".") ,
          Tuple2 ("subsidy 100" , ".") ,
          Tuple2 ("subsidy 200" , ".") ,
          Tuple2 ("subsidy 300" , ".")
        )
      ) ,
      Tuple2 ("actor_children" ,
        Seq (
          Tuple2 ("family A" , "2") ,
          Tuple2 ("family B" , "3") ,
          Tuple2 ("family C" , "1")
        )
      ) ,
      Tuple2 ("actor_adults" ,
        Seq (
          Tuple2 ("family A" , "2") ,
          Tuple2 ("family B" , "1") ,
          Tuple2 ("family C" , "2")
        )
      ) ,
      Tuple2 ("actor_income" ,
        Seq (
          Tuple2 ("family A" , "5000") ,
          Tuple2 ("family B" , "3000") ,
          Tuple2 ("family C" , "800")
        )
      ) ,
      Tuple2 ("resource_value" ,
        Seq (
          Tuple2 ("no subsidy 0" , "0") ,
          Tuple2 ("subsidy 100" , "100") ,
          Tuple2 ("subsidy 200" , "200") ,
          Tuple2 ("subsidy 300" , "300")
        )
      ) ,
      Tuple2 ("outcome" ,
        Seq (
          Tuple2 ("family A" , "subsidy 200") ,
          Tuple2 ("family B" , "subsidy 300") ,
          Tuple2 ("family C" , "subsidy 100") ,
        )
      ) ,
      Tuple2 ("pipelines" ,
        Seq (
          Tuple2 ("CcsNoSubsidyPipeline" , ".") ,
          Tuple2 ("CcsPerChildPipeline" , ".") ,
          Tuple2 ("CcsPerFamilyPipeline" , ".") ,
          Tuple2 ("CcsSingleGuardianPipeline" , ".")
        )
      )
    )
  )

}

case class Example0Instance_ () extends Example0Instance

object Example0Instance {
  def mk : Example0Instance =
    Example0Instance_ ()
}


case class YamlParserSpec ()
  extends
    AnyFunSuite
{

  def check [A ] (obtained : A) (expected : A) : org.scalatest.compatible.Assertion =
    assert (obtained == expected)

  def read_file (file_name : String) : String =
    new String (
      Files .readAllBytes (
        Paths .get (getClass .getResource (file_name) .toURI)
      )
    )

  lazy val parser = YamlParser .mk

  lazy val example0_name = "/example/example0.yaml"

  lazy val example0_contents = read_file (example0_name)

  lazy val example0_instance = Example0Instance .mk .instance

  test ("read example 0") (
    check (
      obtained = parser .parse ( new StringReader (example0_contents) )
    ) (
      expected = example0_instance
    )
  )

}

