package domain.user

import cats.effect.IO
import javax.inject.Inject

class UserService @Inject()(userRepository: UserRepository) {

  def createUser(user: User): IO[Int] =
    userRepository.create(user)

  def getUser(id: String): IO[Option[User]] = userRepository.get(id)

  def deleteUser(id: String): IO[Int] =
    userRepository.delete(id)

  def updateUser(user: User): IO[Int] =
    userRepository.put(user)

}