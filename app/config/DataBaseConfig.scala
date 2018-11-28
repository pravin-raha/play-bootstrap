package config

import cats.effect._
import cats.implicits._
import doobie.hikari._
import doobie.util.ExecutionContexts
import javax.inject.Inject
import pureconfig.loadConfigOrThrow

import scala.concurrent.ExecutionContext

case class DataBaseConfig(
                           driver: String,
                           url: String,
                           username: String,
                           password: String,
                           poolName: String,
                           poolSize: Int
                         )

object DataBaseConfig {

  def dbTransactor(
                                  dataBaseConfig: DataBaseConfig)(implicit cs: ContextShift[IO]): Resource[IO, HikariTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](dataBaseConfig.poolSize) // our connect EC
      te <- ExecutionContexts.cachedThreadPool[IO] // our transaction EC
      xa <- HikariTransactor.newHikariTransactor[IO](
        dataBaseConfig.driver,
        dataBaseConfig.url,
        dataBaseConfig.username,
        dataBaseConfig.password,
        ce,
        te
      )
    } yield xa

}

class DataBaseConfigHolder @Inject() (c : IOContextShift){
  implicit val cs: ContextShift[IO] = c.cs
  val dataBaseConfig: DataBaseConfig = loadConfigOrThrow[_root_.config.DataBaseConfig]("db.default")
}

class IOContextShift{
  val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
}