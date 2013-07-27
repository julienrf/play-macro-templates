import sbt._
import Keys._

object MacroTemplatesBuild extends Build {

  val baseSettings = Seq(
    scalacOptions ++= Seq("-feature", "-Xlint", "-deprecation", "-unchecked"),
    scalaVersion := "2.10.2",
    resolvers += Resolver.sonatypeRepo("snapshots")
  )

  lazy val sample = play.Project("sample") settings (baseSettings: _*) dependsOn macroTemplates

  lazy val macroTemplates = Project("macro-templates", file("macro-templates")) settings (baseSettings: _*) settings (
    scalaVersion := "2.10.3-SNAPSHOT",
    scalaOrganization := "org.scala-lang.macro-paradise",
    libraryDependencies <+= (scalaOrganization, scalaVersion)(_ % "scala-reflect" % _),
    libraryDependencies += "play" %% "play" % "2.1.2"
  )

}
