import sbt.Keys.scalacOptions

lazy val scala3_3 = "3.3.4"

lazy val scala3_5 = "3.5.2"

lazy val commonSettings =
  Seq(
    organization := "se.umu.cs.tiles",
    version := "0.1.0",
    description := "Object-oriented functional language to describe, analyze, and model human-centered problems",
    homepage := Some(url("https://julianmendez.github.io/tiles/")),
    startYear := Some(2023),
    licenses := Seq("Apache License Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    organizationName := "Umea University",
    organizationHomepage := Some(url("https://www.umu.se/en/department-of-computing-science/")),
    developers := List(
      Developer("julianmendez", "Julian Alfredo Mendez", "julian.mendez@gmail.com", new URL("https://julianmendez.github.io"))
    ),
    /**
     * Scala
     * [[https://www.scala-lang.org]]
     * [[https://github.com/scala/scala]]
     * [[https://repo1.maven.org/maven2/org/scala-lang/scala3-compiler_3/]]
     */
    crossScalaVersions := Seq(scala3_3, scala3_5),
    scalaVersion := scala3_3,
    /**
     * ScalaTest
     * [[https://www.scalatest.org]]
     * [[https://github.com/scalatest/scalatest]]
     * [[https://repo1.maven.org/maven2/org/scalatest/]]
     */
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    resolvers += Resolver.mavenLocal,
    publishTo := Some(Resolver.mavenLocal),
    publishMavenStyle := true,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    scalacOptions ++= Seq("-deprecation", "-feature")
  )

lazy val docs =
  project
    .withId("docs")
    .in(file("docs"))
    .settings(commonSettings)

lazy val core =
  project
    .withId("core")
    .in(file("core"))
    .settings(
      commonSettings,
      assembly / assemblyJarName := "core-" + version.value + ".jar"
    )

lazy val examples =
  project
    .withId("examples")
    .in(file("examples"))
    .aggregate(core)
    .dependsOn(core)
    .settings(
      commonSettings,
      /**
       * YAML 1.2 parser
       * [[https://bitbucket.org/asomov/snakeyaml-engine]]
       * [[https://repo1.maven.org/maven2/org/snakeyaml/snakeyaml-engine/]]
       */
      libraryDependencies += "org.snakeyaml" % "snakeyaml-engine" % "2.8",
      assembly / assemblyJarName := "examples-" + version.value + ".jar"
    )

lazy val root =
  project
    .withId("tiles")
    .in(file("."))
    .aggregate(docs, core, examples)
    .dependsOn(docs, core, examples)
    .settings(
      commonSettings,
      assembly / mainClass := Some("soda.tiles.fairness.example.main.EntryPoint"),
      assembly / assemblyJarName := "tiles-" + version.value + ".jar"
    )

