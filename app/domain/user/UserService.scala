package domain.user

import cats.effect.IO
import javax.inject.Inject

class UserService[F[_]] @Inject()(userRepository: UserRepository[F]) {

  def createUser(user: User): F[Int] =
    userRepository.create(user)

  def getUser(id: String): F[Option[User]] = userRepository.get(id)

  def deleteUser(id: String): F[Int] =
    userRepository.delete(id)

  def updateUser(user: User): F[Int] =
    userRepository.put(user)

}