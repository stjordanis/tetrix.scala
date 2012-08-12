import sbt._

object Builds extends Build {
  import Keys._

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1.0-SNAPSHOT",
    organization := "com.eed3si9n",
    homepage := Some(url("http://eed3si9n.com")),
    licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php/")),
    scalaVersion := "2.9.2",
    scalacOptions := Seq("-deprecation", "-unchecked"),
    resolvers ++= Seq(
      "sonatype-public" at "https://oss.sonatype.org/content/repositories/public",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")
  )

  lazy val root = Project("root", file("."),
    settings = buildSettings ++ Seq(name := "tetrix.scala"))
  lazy val library = Project("library", file("library"),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.specs2" %% "specs2" % "1.12" % "test",
        "com.typesafe.akka" % "akka-actor" % "2.0.2")
    ))
  lazy val swing = Project("swing", file("swing"),
    settings = buildSettings ++ Seq(
      fork in run := true,
      libraryDependencies += "org.scala-lang" % "scala-swing" % "2.9.2"
    )) dependsOn(library)
}
