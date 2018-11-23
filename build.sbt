import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := """play-bootstrap""",
    organization := "com.mvpags",
    scalacOptions += "-Ypartial-unification", // 2.11.9+
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.6",
    libraryDependencies ++= doobie,
    libraryDependencies ++= common,
    libraryDependencies ++= playCommon
  )
  .enablePlugins(PlayScala)