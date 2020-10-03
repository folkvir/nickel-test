scalaVersion in ThisBuild := "2.13.3"

lazy val commonSettings = Seq(
  name := "nickel-test",
  organization := "com.example",
  version := "0.1.0-SNAPSHOT",
  mainClass := Some("calculator.CLICalculator"),
  assemblyJarName in assembly := "nickel.jar"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
  "org.backuity.clist" %% "clist-core"   % "3.5.1",
  "org.backuity.clist" %% "clist-macros" % "3.5.1" % "provided",
  "com.typesafe.play" %% "play-json" % "2.9.1"
)

resolvers in Global ++= Seq(
  "Sbt plugins" at "https://dl.bintray.com/sbt/sbt-plugin-releases"
)

lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  enablePlugins(AssemblyPlugin)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}