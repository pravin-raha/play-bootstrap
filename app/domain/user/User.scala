package domain.user

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import play.api.libs.json.{Json, OFormat}

case class User(
                 id: String,
                 name: String,
                 email: String,
                 created: Long,
                 loginInfo: LoginInfo,
                 role: Role
               )  extends Identity

object User {
  implicit val format: OFormat[User] = Json.format[User]
}


