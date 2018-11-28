package controllers

import cats.effect._
import domian.user.UserService
import javax.inject.Inject
import org.pac4j.core.client.IndirectClient
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.credentials.Credentials
import org.pac4j.core.profile.CommonProfile
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala.{Security, SecurityComponents}
import play.api.libs.json._
import play.api.mvc._

class HomeController @Inject()(val controllerComponents: SecurityComponents, userService: UserService)
  extends  Security[CommonProfile] {

  def index() = Secure("FormClient") { implicit request: Request[AnyContent] =>
    userService
      .getUser("101")
      .map {
        case Some(user) => Ok(Json.toJson(user))
        case None => Ok("User Not Found")
      }.unsafeRunSync()

  }


  def loginForm = Action { request =>
    val formClient = config.getClients.findClient("FormClient").asInstanceOf[FormClient]
    Ok(views.html.loginForm.render(formClient.getCallbackUrl))
  }


  def forceLogin = Action { request =>
    val context: PlayWebContext = new PlayWebContext(request, playSessionStore)
    val client = config.getClients.findClient(context.getRequestParameter(Pac4jConstants.DEFAULT_CLIENT_NAME_PARAMETER)).asInstanceOf[IndirectClient[Credentials,CommonProfile]]
    Redirect(client.getRedirectAction(context).getLocation)
  }
}