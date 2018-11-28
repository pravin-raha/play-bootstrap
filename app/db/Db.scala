package db

import cats.effect.{ContextShift, IO, Resource}
import config.{DataBaseConfigHolder, IOContextShift}
import doobie.hikari.HikariTransactor
import javax.inject.Inject

class Db @Inject()(config: DataBaseConfigHolder, c: IOContextShift) {
  implicit val cs: ContextShift[IO] = c.cs
  val transactor: Resource[IO, HikariTransactor[IO]] = _root_.config.DataBaseConfig.dbTransactor(config.dataBaseConfig)
}
