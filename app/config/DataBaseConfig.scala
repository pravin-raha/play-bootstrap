package config

import cats.effect._
import cats.implicits._
import doobie.hikari._
import doobie.util.ExecutionContexts

case class DataBaseConfig(
                           driver: String,
                           url: String,
                           username: String,
                           password: String,
                           poolName: String,
                           poolSize: Int
                         )

object DataBaseConfig {

  def dbTransactor[F[_] : Async](
                                  dataBaseConfig: DataBaseConfig)(implicit cs: ContextShift[F]): Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](dataBaseConfig.poolSize) // our connect EC
      te <- ExecutionContexts.cachedThreadPool[F] // our transaction EC
      xa <- HikariTransactor.newHikariTransactor[F](
        dataBaseConfig.driver,
        dataBaseConfig.url,
        dataBaseConfig.username,
        dataBaseConfig.password,
        ce,
        te
      )
    } yield xa

}