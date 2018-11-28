package domian.user

import cats.effect.IO
import com.google.inject.ImplementedBy
import repository.doobie.UserRepositoryInterpreter

@ImplementedBy(classOf[UserRepositoryInterpreter])
trait UserRepository {

  def create(user: User): IO[Int]

  def get(id: String): IO[Option[User]]

  def delete(id: String): IO[Int]

  def put(user: User): IO[Int]
}