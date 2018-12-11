package domain.user

import cats.effect.IO
import com.google.inject.ImplementedBy
import repository.doobie.UserRepositoryInterpreter

@ImplementedBy(classOf[UserRepositoryInterpreter])
trait UserRepository[F[_]] {

  def create(user: User): F[Int]

  def get(id: String): F[Option[User]]

  def delete(id: String): F[Int]

  def put(user: User): F[Int]
}