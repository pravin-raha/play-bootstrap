package domain.user

import enumeratum.values.{StringEnum, StringEnumEntry, StringPlayJsonValueEnum}

import scala.collection.immutable

sealed abstract class Role(override val value: String) extends StringEnumEntry {
  override def toString: String = value
}

object Role extends StringEnum[Role] with StringPlayJsonValueEnum[Role] {
  def apply(role: String): Role = Role.withValue(role)

  def unapply(role: Role): Option[String] = Some(role.toString)

  override def values: immutable.IndexedSeq[Role] = findValues

  case object Admin extends Role("admin")

  case object NormalUser extends Role("user")

}