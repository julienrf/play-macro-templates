import sbt._
import Keys._

object MacroTemplatesBuild extends Build {

  val baseSettings = Seq(
    scalacOptions ++= Seq("-feature", "-Xlint", "-deprecation", "-unchecked"),
    scalaVersion := "2.10.2",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    addCompilerPlugin("org.scala-lang.plugins" % "macro-paradise" % "2.0.0-SNAPSHOT" cross CrossVersion.full)
  )

  lazy val sample = play.Project("sample") settings (baseSettings: _*) dependsOn macroTemplates

  lazy val macroTemplates = Project("macro-templates", file("macro-templates")) settings (baseSettings: _*) settings (
    libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
    libraryDependencies += "play" %% "play" % "2.1.2"
  )

}
