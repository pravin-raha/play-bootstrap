package controllers

import cats.effect._
import domian.user.UserService
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc._

class HomeController @Inject()(cc: ControllerComponents, userService: UserService)
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