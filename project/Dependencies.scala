import play.sbt.PlayImport._
import sbt._

object Dependencies {
  lazy val doobieVersion = "0.6.0"
  lazy val flywayDb = "5.1.4"

  val playVersion = "2.6.18"
  val playPac4jVersion = "6.1.0"
  val pac4jVersion = "3.4.0"

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

  val pac4j = Seq(
    guice,
    ehcache, // or cacheApi
    ws,
    filters,
    "org.pac4j" %% "play-pac4j" % playPac4jVersion,
    "org.pac4j" % "pac4j-http" % pac4jVersion,
    "org.pac4j" % "pac4j-cas" % pac4jVersion,
    "org.pac4j" % "pac4j-openid" % pac4jVersion exclude("xml-apis" , "xml-apis"),
    "org.pac4j" % "pac4j-oauth" % pac4jVersion,
//    "org.pac4j" % "pac4j-saml" % pac4jVersion,
    "org.pac4j" % "pac4j-oidc" % pac4jVersion exclude("commons-io" , "commons-io"),
    "org.pac4j" % "pac4j-gae" % pac4jVersion,
    "org.pac4j" % "pac4j-jwt" % pac4jVersion exclude("commons-io" , "commons-io"),
    "org.pac4j" % "pac4j-ldap" % pac4jVersion,
    "org.pac4j" % "pac4j-sql" % pac4jVersion,
    "org.pac4j" % "pac4j-mongo" % pac4jVersion,
    "org.pac4j" % "pac4j-kerberos" % pac4jVersion,
    "org.pac4j" % "pac4j-couch" % pac4jVersion,
    "org.apache.shiro" % "shiro-core" % "1.4.0",
    "com.typesafe.play" %% "play-cache" % playVersion,
    "commons-io" % "commons-io" % "2.5"
  )

  val playCommon = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  )

  val playSwagger = Seq(
    "org.webjars" % "swagger-ui" % "2.2.0"
  )
}