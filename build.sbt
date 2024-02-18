import sbt.Keys.scalacOptions

lazy val scala3_4 = "3.4.0"

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
    crossScalaVersions := Seq(scala3_4),
    scalaVersion := scala3_4,
    /**
     * ScalaTest
     * [[https://www.scalatest.org]]
     * [[https://github.com/scalatest/scalatest]]
     * [[https://repo1.maven.org/maven2/org/scalatest/]]
     */
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test",
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

lazy val root =
  project
    .withId("tiles")
    .in(file("."))
    .settings(
      commonSettings,
      assembly / assemblyJarName := "tiles-" + version.value + ".jar"
    )


