package controllers

import cats.effect._
import domian.user.UserService
import play.api.libs.json._
import play.api.mvc._

class HomeController(cc: ControllerComponents, userService: UserService[IO])
  extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    userService
      .getUser("101")
      .map {
        case Some(user) => Ok(Json.toJson(user))
        case None => Ok("User Not Found")
      }.unsafeRunSync()

  }

}