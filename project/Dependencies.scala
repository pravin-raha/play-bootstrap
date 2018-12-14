import play.sbt.PlayImport._
import sbt._

object Dependencies {
  lazy val doobieVersion = "0.6.0"
  lazy val flywayDb = "5.1.4"
  lazy val circeVersion = "0.10.0"

  lazy val playVersion = "2.6.18"
  lazy val playPac4jVersion = "7.0.0-SNAPSHOT"
  lazy val pac4jVersion = "3.4.0"
  lazy val enumeratumPlayJsonVersion = "1.5.13"
  lazy val silhouetteVersion = "5.0.6"

  lazy val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-specs2" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.tpolecat" %% "doobie-h2" % doobieVersion,
    "org.flywaydb" %% "flyway-play" % "5.1.0",
    "mysql" % "mysql-connector-java" % "5.1.24",
    "io.chrisdavenport" %% "fuuid-doobie" % "0.2.0-M2"
  )

  lazy val common: Seq[ModuleID] = Seq(
    "mysql" % "mysql-connector-java" % "5.1.24",
    "org.typelevel" %% "cats-core" % "1.4.0",
    "org.typelevel" %% "cats-effect" % "1.0.0",
    "com.github.pureconfig" %% "pureconfig" % "0.9.2",
    "io.chrisdavenport" %% "log4cats-slf4j" % "0.1.1",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "io.scalaland" %% "chimney" % "0.2.1",
    "com.softwaremill.macwire" %% "macros" % "2.3.1",
    "com.typesafe.play" %% "play-json" % "2.6.10"
  ) ++ circe

  lazy val pac4j: Seq[ModuleID] = Seq(
    guice,
    ehcache, // or cacheApi
    ws,
    filters,
    "org.pac4j" %% "play-pac4j" % playPac4jVersion,
    "org.pac4j" % "pac4j-http" % pac4jVersion,
    "org.pac4j" % "pac4j-cas" % pac4jVersion,
    "org.pac4j" % "pac4j-openid" % pac4jVersion exclude("xml-apis", "xml-apis"),
    "org.pac4j" % "pac4j-oauth" % pac4jVersion,
    //    "org.pac4j" % "pac4j-saml" % pac4jVersion,
    "org.pac4j" % "pac4j-oidc" % pac4jVersion exclude("commons-io", "commons-io"),
    "org.pac4j" % "pac4j-gae" % pac4jVersion,
    "org.pac4j" % "pac4j-jwt" % pac4jVersion exclude("commons-io", "commons-io"),
    "org.pac4j" % "pac4j-ldap" % pac4jVersion,
    "org.pac4j" % "pac4j-sql" % pac4jVersion,
    "org.pac4j" % "pac4j-mongo" % pac4jVersion,
    "org.pac4j" % "pac4j-kerberos" % pac4jVersion,
    "org.pac4j" % "pac4j-couch" % pac4jVersion,
    "org.apache.shiro" % "shiro-core" % "1.4.0",
    "com.typesafe.play" %% "play-cache" % playVersion,
    "commons-io" % "commons-io" % "2.5"
  )

  lazy val playCommon: Seq[ModuleID] = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    "com.beachape" %% "enumeratum-play-json" % enumeratumPlayJsonVersion
  ) ++ silhouette

  lazy val playSwagger: Seq[ModuleID] = Seq(
    "org.webjars" % "swagger-ui" % "2.2.0"
  )

  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

  lazy val silhouette = Seq(
    "com.mohiva" %% "play-silhouette" % silhouetteVersion excludeAll ExclusionRule(organization = "com.atlassian.jwt"),
    "com.mohiva" %% "play-silhouette-password-bcrypt" % silhouetteVersion excludeAll ExclusionRule(organization = "com.atlassian.jwt"),
    "com.mohiva" %% "play-silhouette-persistence" % silhouetteVersion excludeAll ExclusionRule(organization = "com.atlassian.jwt"),
    "com.mohiva" %% "play-silhouette-crypto-jca" % silhouetteVersion excludeAll ExclusionRule(organization = "com.atlassian.jwt")
  )

}