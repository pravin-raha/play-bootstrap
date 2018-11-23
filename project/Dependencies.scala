import play.sbt.PlayImport._
import sbt._

object Dependencies {
  lazy val doobieVersion = "0.6.0"
  lazy val flywayDb = "5.1.4"

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-specs2" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.tpolecat" %% "doobie-h2" % doobieVersion,
    "org.flywaydb" %% "flyway-play" % "5.1.0",
    "mysql" % "mysql-connector-java" % "5.1.24"
  )

  val common = Seq(
    "mysql" % "mysql-connector-java" % "5.1.24",
    "org.typelevel" %% "cats-core" % "1.4.0",
    "org.typelevel" %% "cats-effect" % "1.0.0",
    "com.github.pureconfig" %% "pureconfig" % "0.9.2",
    "io.chrisdavenport" %% "log4cats-slf4j" % "0.1.1",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "io.scalaland" %% "chimney" % "0.2.1",
    "com.softwaremill.macwire" %% "macros" % "2.3.1",
    "com.typesafe.play" %% "play-json" % "2.6.10"
  )

  val pac4jOidc = "org.pac4j" % "pac4j-oidc" % "3.2.0"

  val playCommon = Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  )

}