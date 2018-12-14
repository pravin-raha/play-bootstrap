package repository.doobie

import cats.effect.{ContextShift, IO}
import config.IOContextShift
import db.Db
import domain.user.{User, UserRepository}
import doobie.implicits._
import doobie.util.log.LogHandler
import doobie.util.update.Update0
import javax.inject.{Inject, Singleton}

private object UserSql {
  def insert(user: User): Update0 =
    sql"insert into USER (id, name, email, created, role) values (${user.id}, ${user.name}, ${user.email},, ${user.created}, ${user.role})".updateWithLogHandler(LogHandler.jdkLogHandler)

  def select(id: String): doobie.Query0[User] = sql"""
    SELECT *
    FROM USER
    WHERE ID = $id
  """.queryWithLogHandler[User](LogHandler.jdkLogHandler)


  def delete(id: String): doobie.Update0 = sql"delete from USER where id = $id".updateWithLogHandler(LogHandler.jdkLogHandler)

  def update(user: User): doobie.Update0 = sql"update USER set id = ${user.id}, name = ${user.name}, email = ${user.email} where id = ${user.id}".updateWithLogHandler(LogHandler.jdkLogHandler)
}

@Singleton
class UserRepositoryInterpreter @Inject()(db: Db, c: IOContextShift)
  extends UserRepository[IO] {

  import UserSql._

  implicit val cs: ContextShift[IO] = c.cs

  override def create(user: User): IO[Int] = db.transactor.use(xa => insert(user).run.transact(xa))

  override def get(id: String): IO[Option[User]] = db.transactor.use(xa => select(id).option.transact(xa))

  override def delete(id: String): IO[Int] = db.transactor.use(xa => UserSql.delete(id).run.transact(xa))

  override def put(user: User): IO[Int] = db.transactor.use(xa => UserSql.update(user).run.transact(xa))

}