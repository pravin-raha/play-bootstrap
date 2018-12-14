package controllers

import cats.effect._
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import domain.auth.JWTEnv
import domain.user.UserService
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
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HomeController @Inject()(val controllerComponents: SecurityComponents, userService: UserService[IO])
  extends  Security[CommonProfile] {

  def index(): Action[AnyContent] = Secure("OidcClient") { implicit request: Request[AnyContent] =>
//    userService
//      .getUser("101")
//      .map {
//        case Some(user) => Ok(Json.toJson(user))
//        case None => Ok("User Not Found")
//      }.unsafeRunSync()

    Ok("Ok")

  }

//  def index(): Action[AnyContent] = Action.async { implicit request =>
//    silhouette.SecuredRequestHandler { securedRequest =>
//      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
//    }.map {
//      case HandlerResult(r, Some(user)) => Ok(Json.toJson(user.id))
//      case HandlerResult(r, None) => Unauthorized
//    }
//  }

  def loginForm = Action { _ =>
    val formClient = config.getClients.findClient("OidcClient").asInstanceOf[FormClient]
    Ok(views.html.loginForm.render(formClient.getCallbackUrl))
  }

  def forceLogin = Action { request =>
    val context: PlayWebContext = new PlayWebContext(request, playSessionStore)
    val client = config.getClients.findClient(context.getRequestParameter(Pac4jConstants.DEFAULT_CLIENT_NAME_PARAMETER)).asInstanceOf[IndirectClient[Credentials, CommonProfile]]
    Redirect(client.getRedirectAction(context).getLocation)
  }
}