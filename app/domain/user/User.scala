package domain.user

import play.api.libs.json.{Json, OFormat}

case class User(
                 id: Option[String],
                 name: String,
                 email: String
               )

object User {
  implicit val format: OFormat[User] = Json.format[User]
}